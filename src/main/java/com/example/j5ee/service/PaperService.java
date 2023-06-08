package com.example.j5ee.service;

import com.example.j5ee.entity.Paper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface PaperService {

    int insertPaper(int id,String title, String author,String resource, int type,
                    String summary, Date publishDate, Date uploadDate
            , String download, String link);

    int uploadFile(MultipartFile multipartFile, String title,int id) throws IOException;

    int updateInfo(Paper paper,int id);


    int setUrl(int pid);

    Paper getPaperByPid(int id,int pid);

    void deletePaper(int id,int pid);

    void revisePaper(int id, int pid);

    int checkPaperStatus(int id, int pid);

    void judgePaper(int id,int pid,int score);

    void setPaperStatus(int id,int pid,int status,int score);


    List<Paper> getPapersByScores(int id,int min,int max);

    Map<Integer,List<Paper>>  getPapersListByScores(int id,int min,int max);

    List<Paper> getAllPapers(int index,int size);

    List<Paper> getPapers(int id,int index,int size);

    List<Paper> getPapers2(int id);

    Integer[] getPapersByMonth(int id,int year);

    List<Paper> getPapersNotSet(int id);

}
