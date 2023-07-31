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
    TEST_CUSTOM_EXCEPTION(1000, "테스트용 예외처리");

    // USER (2000 ~ 2999)

    // BOARD (3000 ~ 3999)

    private final Integer code;
    private final String message;
}
