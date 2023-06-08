package com.example.j5ee.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.j5ee.common.CommonConstants;
import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.Search;
import com.example.j5ee.entity.User;
import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.mapper.UserMapper;
import com.example.j5ee.service.PaperService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    /**
     * 添加论文
     * 未审核是0，pass通过审核是，未通过是-1
     * 论文类型type怎么初始化
     */
    @Override
    public int insertPaper(int id,String title, String author,String resource, int type,
                           String summary, Date publishDate, Date uploadDate
                             , String download, String link) {
        //  ... .... ...String title,  String author,String resource, int type,
        ////                   String summary, Date publishDate, Date uploadDate,Date deleteDate, int collections, int likes
        ////            , String download, String link,  int score,  int status
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }
        Paper paper = Paper.builder()
                .title(title).publishDate(publishDate).id(id).author(author).resource(resource).type(type).summary(summary)
                .uploadDate(uploadDate).download(download).link(link).status(0).collections(0).likes(0)
                .build();
        int rows = paperMapper.insertPaper(paper);
        if(rows!=1){
            throw new CommonException(CommonErrorCode.PAPER_INSERT_ERROR);
        }
        //
        try {
            Search search = new Search(title,author,summary);
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout("2m");
            bulkRequest.add(new IndexRequest("paper_list")
                    .source(JSON.toJSONString(search), XContentType.JSON)
            );
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e){
            throw new CommonException(CommonErrorCode.SEARCH_INSERT_ERROR);
        }

        return rows;
    }

//    @Override
//    public int uploadFile(MultipartFile multipartFile, String title,int id) throws IOException {
//        Paper paper = paperMapper.getPaperByTitle(title);
//        int pid = paper.getPid();
//        String originalFilename = multipartFile.getOriginalFilename();
//        String rootFilePath ="home/ubuntu/file/paper"+paper.getPid()+"-"+originalFilename;
//        try{
//            FileUtil.writeBytes(multipartFile.getBytes(),rootFilePath);
//            //
//            String path ="paper"+pid+"-"+multipartFile.getOriginalFilename();
//            int index = multipartFile.getOriginalFilename().indexOf(".");//获取第一个"."的位置
//            String suffix = multipartFile.getOriginalFilename().substring(index);
//            paperMapper.setDownload(suffix,pid);
//        }catch (IOException e) {
//            throw new IOException("读取失败");
//        }
//        return paper.getPid();
//    }

    @Override
    public int uploadFile(MultipartFile multipartFile, String title,int id) throws IOException {
        Paper paper = paperMapper.getPaperByTitle(title);
        if(paper==null || paper.getId()!=id){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        int pid = paper.getPid();
        String originalFilename = multipartFile.getOriginalFilename();
        String rootFilePath ="home/ubuntu/file/paper"+pid+"-"+originalFilename;
        try{
            FileUtil.writeBytes(multipartFile.getBytes(),rootFilePath);
            //
            String path ="paper"+pid+"-"+originalFilename;
            int index = originalFilename.indexOf(".");//获取第一个"."的位置
            String suffix = originalFilename.substring(index);
            //set_download_1
            if(paperMapper.setDownload(suffix,pid)==0){
                throw new CommonException(CommonErrorCode.SYSTEM_INTERNAL_ANOMALY);
            }
            putCOS(pid,path,suffix);
        }catch (IOException e)
        {
            throw new CommonException(CommonErrorCode.FILE_UPLOAD_ERROR);
        }
        return pid;
    }

    public static void putCOS(int pid,String path,String suffix){
        //本机地址path
        path = "/home/ubuntu/j5ee-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/home/ubuntu/file/"+path;
        COSClient cosClient = creatCOSClient(CommonConstants.SECRETID, CommonConstants.SECRECTKEY,CommonConstants.COS_REGION);
        String key ="paper/"+pid+suffix;
        uploadCOS(cosClient,path, CommonConstants.BUCKET,key);
        cosClient.shutdown();
    }

    public static COSClient creatCOSClient(String secretId,String secretKey,String cosRegion) {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(cosRegion));
        return new COSClient(cred,clientConfig);
    }

    public static PutObjectResult uploadCOS(COSClient cosClient, String localFilepath, String Bucketname, String key) {
        // 指定要上传的文件
        File localFile = new File(localFilepath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(Bucketname, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return putObjectResult;
    }

    @Override
    public int setUrl(int pid) {
        String url = CommonConstants.PAPER_DOWNLOAD_URL;
        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper.getDownload()==null){
            throw new CommonException(CommonErrorCode.FILE_NOT_FOUND);
        }
        String suffix = paper.getDownload();
        url = url + pid + suffix;
        log.info("url--------------------"+url);
        return paperMapper.setDownload(url,pid);
    }

    @Override
    public Paper getPaperByPid(int id,int pid){
        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //id不匹配的普通用户不能得到论文
        if(user.getType()!=0 && user.getId()!=paper.getId())
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        return paper;
    }

    /**
     * 删除论文
     */
    @Override
    public void deletePaper(int id,int pid) {
        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //普通用户删除非本人上传的论文，不允许
        if(user.getType()!=0 && user.getId()!=paper.getId())
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        //删除
        if(paperMapper.deleteByPid(pid)!=1)
            throw new CommonException(CommonErrorCode.PAPER_DELETE_ERROR);
    }

    /**
     * 修改论文信息
     * 参数有哪些，传递paper的所有属性？
     */
    @Override
    public void revisePaper(int id,int pid) {

        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        //普通用户更改非本人上传的论文，不允许
        if(user.getType()!=0 && id!=paper.getId())throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

       if(paperMapper.updateInfo(paper)!=1)throw new CommonException(CommonErrorCode.PAPER_UPDATE_ERROR);
    }

    /**
     * 查看论文状态
     *
     */
    @Override
    public int checkPaperStatus(int id,int pid) {
        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //普通用户查看非本人上传的论文状态，不允许
        if(user.getType()!=0 && id!=paper.getId())
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        return paper.getStatus();
    }

    /**
     * 管理员设置论文审核状态
     */
    public void setPaperStatus(int id,int pid,int status,int score){
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }
        //用户权限检查
        if(user.getType()!=0)
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        //status的值是否符合要求
        if(status!=1 && status!=0 && status!=-1){
            throw new CommonException(CommonErrorCode.PAPER_STATUS_ERROR);
        }

        //设置分数
        judgePaper(id,pid,score);

       //设置状态
        paper.setStatus(status);
        if(paperMapper.updateInfo(paper)!=1)
            throw new CommonException(CommonErrorCode.PAPER_UPDATE_ERROR);
    }



    /**
     * 管理员设置分数
     *
     */
    @Override
    public void judgePaper(int id,int pid,int score) {
        Paper paper = paperMapper.getPaperByPid(pid);
        if(paper==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //用户权限检查
        if(user.getType()!=0)throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        Paper paperByPid = paperMapper.getPaperByPid(paper.getPid());
        if(paperByPid==null) throw new CommonException(CommonErrorCode.PAPER_NOT_EXIST);
        paperByPid.setScore(score);
        if(paperMapper.updateInfo(paperByPid)!=1)
            throw new CommonException(CommonErrorCode.PAPER_UPDATE_ERROR);
    }

    /**
     * 管理员根据分数进行统计，获得[min,max]分数段的论文集合
     *
     */
    @Override
    public List<Paper> getPapersByScores(int id, int min, int max) {

        User user = userMapper.getUserById(id);
        if(user==null)throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //用户权限检查
        if(user.getType()!=0)throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        List<Paper> papersByScoreScope = paperMapper.getPapersByScoreScope(min, max);
     //   if(papersByScoreScope==null)
        return papersByScoreScope;

    }

    /**
     * 管理员管理论文：（制作分数表）返回map，key为分数，value为拥有相应分数的论文集合
     * 如果还没有打分怎么办
     */
    @Override
    public Map<Integer, List<Paper>> getPapersListByScores(int id, int min, int max) {
        User user = userMapper.getUserById(id);
        if(user==null)
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //用户权限检查
        if(user.getType()==1)
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        Map<Integer, List<Paper>> map = new HashMap<>();
        List<Paper> paperList = paperMapper.getPapersByScoreScope(min, max);
        for(int i = min;i<=max;i++){
            map.put(i,new ArrayList<>());
        }
        for(Paper p:paperList){
            //此处还需要判断是否已经打分
            map.get(p.getScore()).add(p);
        }
        return map;

    }

    @Override
    public List<Paper> getAllPapers(int index, int size) {
        return paperMapper.getAllPage(index,size);
    }

    @Override
    public List<Paper> getPapers(int id,int index, int size) {
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        return paperMapper.getPage(id,index,size);
    }

    @Override
    public List<Paper> getPapers2(int id) {
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        return paperMapper.getPapers(id);
    }

    @Override
    public Integer[] getPapersByMonth(int id, int year) {
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }
        if(user.getType()==1)
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        LinkedHashMap<String, Integer> papersByMonth = paperMapper.getPapersByMonth(year);
        Integer[] sum = new Integer[12];
        sum[0]=Integer.parseInt(String.valueOf(papersByMonth.get("Jan")));
        sum[1]=Integer.parseInt(String.valueOf(papersByMonth.get("Feb")));
        sum[2]=Integer.parseInt(String.valueOf(papersByMonth.get("Mar")));
        sum[3]=Integer.parseInt(String.valueOf(papersByMonth.get("Apr")));
        sum[4]=Integer.parseInt(String.valueOf(papersByMonth.get("May")));
        sum[5]=Integer.parseInt(String.valueOf(papersByMonth.get("June")));
        sum[6]=Integer.parseInt(String.valueOf(papersByMonth.get("July")));
        sum[7]=Integer.parseInt(String.valueOf(papersByMonth.get("Aug")));
        sum[8]=Integer.parseInt(String.valueOf(papersByMonth.get("Sept")));
        sum[9]=Integer.parseInt(String.valueOf(papersByMonth.get("Oct")));
        sum[10]=Integer.parseInt(String.valueOf(papersByMonth.get("Nov")));
        sum[11]=Integer.parseInt(String.valueOf(papersByMonth.get("Dece")));
        return sum;

    }

    @Override
    public List<Paper> getPapersNotSet(int id) {
        User user = userMapper.getUserById(id);
        if(user==null)
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);

        //用户权限检查
        if(user.getType()==1)
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);

        List<Paper> papersNotSet = paperMapper.getPapersNotSet();
        return papersNotSet;
    }

    @Override
    public int updateInfo(Paper paper, int id) {
        if(paper.getId()!=id){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        System.out.println(paper);
        return paperMapper.updateInfo(paper);
    }
}
