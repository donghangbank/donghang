package bank.donghang.donghang_api.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_REQUEST(1_000, "유효하지 않은 요청입니다."),

    MEMBER_NOT_FOUND(2_000, "존재하지 않는 유저입니다.")
    ;

    private final int code;
    private final String message;
}
