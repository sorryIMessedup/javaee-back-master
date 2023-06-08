package com.example.j5ee.common;

import lombok.Getter;

/***
 * @author Urmeas
 * @date 2022/10/1 12:31 上午
 */
@Getter
public class CommonException extends RuntimeException{

    private CommonErrorCode commonErrorCode;

    private Object errorMsg;

    public CommonException() {
    }

    public CommonException(CommonErrorCode commonErrorCode) {
        this.commonErrorCode = commonErrorCode;
    }


    public CommonException(CommonErrorCode commonErrorCode, Object errorMsg) {
        this.commonErrorCode = commonErrorCode;
        this.errorMsg = errorMsg;
    }
}
