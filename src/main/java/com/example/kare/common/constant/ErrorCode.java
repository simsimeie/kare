package com.example.kare.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ErrorCode {
    SUCCESS(200, ErrorCategory.SUCCESS, "성공"),
    BAD_REQUEST(400, ErrorCategory.CLIENT_SIDE, "입력하신 값을 다시 한 번 확인해주세요."),
    SERVER_ERROR(500, ErrorCategory.SERVER_SIDE, "데이터 처리 중 에러가 발생했습니다.");

    private final Integer code;
    private final ErrorCategory category;
    private final String message;

    ErrorCode(Integer code, ErrorCategory category, String message) {
        this.code = code;
        this.category = category;
        this.message = message;
    }

    public boolean isClientSideError() {
        return this.getCategory() == ErrorCategory.CLIENT_SIDE;
    }

    enum ErrorCategory {
        SUCCESS, CLIENT_SIDE, SERVER_SIDE
    }
}
