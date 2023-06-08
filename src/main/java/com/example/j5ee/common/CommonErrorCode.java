package com.example.j5ee.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/***
 * @author Urmeas
 * @date 2022/10/1 12:30 上午
 */
@Getter
public enum CommonErrorCode {
   //系统
   SYSTEM_INTERNAL_ANOMALY(4000,"数据层处理错误","网络不给力，请稍后重试。"),

   //登录
   USER_NOT_EXIST(1000,"用户不存在","用户不存在"),
   INVALID_SESSION(1001,"会话丢失","登录已失效，请重新登录"),
   LOGIN_FAILED(1002,"用户名或密码错误","用户名或密码错误"),

   //注册
   REGISTER_ALREADY_EXIST(1005,"用户名重复","用户名重复"),

   //mail
   MAIL_USED_ERROR(2000,"邮箱号已注册","请前往登录"),
   VERIFY_FAILED(2001,"验证码错误","验证码错误"),
   INVALID_MAIL(2002,"无效邮箱号","请输入正确的邮箱号"),
   MAIL_NOT_EXIST(2003,"用户插入失败","邮箱验证失败，请重试"),
   MAIL_SEND_ERROR(2004,"mail发送失败","邮箱验证失败,请重试"),


   //图片
   INVALID_PICTURE_TYPE(2010,"无效的图片类型","图片上传出错，请重试"),
   UPLOAD_IMG_ERROR(2011,"服务器上传图片文件失败","图片上传出错，请重试"),

   //paper
   PAPER_INSERT_ERROR(2020,"论文插入失败","论文上传出错，请重试"),
   PAPER_NOT_EXIST(2021,"论文不存在","论文不存在，请重试"),
   PAPER_UPDATE_ERROR(2022,"论文信息更新失败","论文信息更新失败，请重试"),
   PAPER_DELETE_ERROR(2023,"论文删除失败","论文删除失败"),
   PAPER_STATUS_ERROR(2024,"论文审核状态不符合要求","论文审核状态不符合要求"),
   //user
   USER_PERMISSION_DENIED(2030,"当前用户无权限","当前用户无权限"),
   USER_UPDATE_FAILED(2031,"用户信息更新错误","用户信息更新错误"),
   //参数
    PARAMS_ERROR(2040,"参数错误","参数错误"),

   //subject
   SUBJECT_INSERT_ERROR(2050,"项目插入失败","项目插入失败"),
   SUBJECT_FORMAT_ERROR(2051,"格式错误","格式错误"),
   SUBJECT_NOT_EXIT(2052,"项目不存在","项目不存在"),
   SUBJECT_UPDATE_ERROR(2053,"论文更新失败","论文更新失败"),
   SUBJECT_DELETE_ERROR(2054,"删除失败","删除失败"),

   //search
   SEARCH_INSERT_ERROR(3000,"search插入失败","search插入失败"),

   //文件
   FILE_UPLOAD_ERROR(4000,"文件上传失败","文件上传失败"),
   FILE_NOT_FOUND(4001,"文件未找到","文件未找到"),

   ;

   /**
    * 错误码
    */
   private final Integer errorCode;

   /**
    * 错误原因（给开发看的）
    */
   private final String errorReason;

   /**
    * 错误行动指示（给用户看的）
    */
   private final String errorSuggestion;

   CommonErrorCode(Integer errorCode, String errorReason, String errorSuggestion) {
      this.errorCode = errorCode;
      this.errorReason = errorReason;
      this.errorSuggestion = errorSuggestion;
   }

   @Override
   public String toString() {
      return "CommonErrorCode{" +
              "errorCode=" + errorCode +
              ", errorReason='" + errorReason + '\'' +
              ", errorSuggestion='" + errorSuggestion + '\'' +
              '}';
   }

   //use for json serialization
   public Map<String,Object> toMap(){
      Map<String,Object> map = new HashMap<>();
      map.put("errorCode",errorCode);
      map.put("errorReason",errorReason);
      map.put("errorSuggestion",errorSuggestion);
      return map;
   }

}
