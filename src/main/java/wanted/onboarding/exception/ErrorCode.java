package wanted.onboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * 도메인별로 Exception 정리
     * */

    // TEST (1000)
    TEST_CUSTOM_EXCEPTION(1000, "테스트용 예외처리"),

    // USER (2000 ~ 2999)
    INVALID_EMAIL(2000, "이메일 형식이 올바르지 않습니다"),
    INVALID_PASSWORD(2001, "비밀번호 길이가 부족합니다"),
    DUPLICATE_EMAIL(2002, "중복된 이메일입니다"),
    NOT_FOUND_USER(2003, "로그인한 사용자를 찾을 수 없습니다");

    // BOARD (3000 ~ 3999)

    private final Integer code;
    private final String message;
}
