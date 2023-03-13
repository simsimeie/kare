package com.example.kare.common.dto;

import com.example.kare.common.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
public class ResponseDto<T> extends ResponseCommonDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private T body;
    private ResponseDto(T body){
        super(ErrorCode.SUCCESS);
        this.body = body;
    }
    public static <T> ResponseDto<T> of(T body){
        return new ResponseDto(body);
    }

}
