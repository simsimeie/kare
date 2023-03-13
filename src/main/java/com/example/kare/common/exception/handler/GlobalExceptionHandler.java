package com.example.kare.common.exception.handler;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.dto.ResponseCommonDto;
import com.example.kare.common.dto.ResponseDto;
import com.example.kare.common.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*
        KBException에 대한 공통 처리 로직
    */
    @ExceptionHandler
    public ResponseEntity<Object> kbExceptionHandler(KBException ex, WebRequest request){
        HttpStatus status = getHttpStatus(ex);
        ResponseCommonDto body = getBodyForException(ex);

        return super.handleExceptionInternal(
                ex
                ,body
                ,HttpHeaders.EMPTY
                ,status
                ,request
        );
    }

    private HttpStatus getHttpStatus(KBException ex){
        ErrorCode errorCode = ex.getErrorCode();

        if(errorCode.isClientSideError()){
            return HttpStatus.BAD_REQUEST;
        }else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private ResponseCommonDto getBodyForException(KBException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = null;

        if(ex.getMessage() != null){
            message = ex.getMessage();
        }else{
            message = errorCode.getMessage();
        }

        return new ResponseCommonDto(errorCode, message);
    }

    /*
        @Valid 유효성 검사 실패시 발생하는 Exception에 대한 공통 처리 로직
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        status =  HttpStatus.BAD_REQUEST;

        return super.handleExceptionInternal(
                ex
                , new ResponseCommonDto(errorCode, ex.getFieldError().getDefaultMessage())
                , headers
                , status
                , request
        );
    }

    /*
        Spring MVC에서 발생하는 Exception에 대한 공통 처리 로직
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorCode errorCode = status.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.SERVER_ERROR;

        return super.handleExceptionInternal(
                ex
                , new ResponseCommonDto(errorCode, ex.getMessage())
                , HttpHeaders.EMPTY
                , status
                , request
        );
    }

    /*
        // 사용사례
        KBException이 아니고, Spring MVC에서 처리된 Exception이 아닐 때
        // 우려사항
        위에서 catch하지 못한 Exception이 발생하면, 일관성 있는 Response를 제공하지 못하므로
        // 해법
        공통 Response 형태를 사용하기 위해 모든 Exception을 Handling 하는 로직을 만들어 처리한다.
        // 단점
        위에서 catch하지 못한 Exception들은 모두 'http status를 500, message는 처리 중 오류가 발생했습니다.' 로 통일되어 보여짐
     */
    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(Exception ex, WebRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return super.handleExceptionInternal(
                ex
                ,new ResponseCommonDto(ErrorCode.SERVER_ERROR)
                ,HttpHeaders.EMPTY
                ,status
                ,request
        );
    }
}


