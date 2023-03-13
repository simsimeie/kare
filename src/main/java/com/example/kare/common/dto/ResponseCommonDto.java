package com.example.kare.common.dto;

import com.example.kare.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ResponseCommonDto {
    private final Boolean ok;
    private final Integer code;
    private final String message;
    public ResponseCommonDto(ErrorCode errorCode){
        if(errorCode == ErrorCode.SUCCESS){
            ok = Boolean.TRUE;
        } else{
            ok = Boolean.FALSE;
        }
        code = errorCode.getCode();
        message = errorCode.getMessage();
    }

    public ResponseCommonDto(ErrorCode errorCode, String message){
        if(errorCode == ErrorCode.SUCCESS){
            ok = Boolean.TRUE;
        } else{
            ok = Boolean.FALSE;
        }
        code = errorCode.getCode();
        this.message = message;
    }

    public ResponseCommonDto(Boolean ok, Integer code, String message) {
        this.ok = ok;
        this.code = code;
        this.message = message;
    }
}
