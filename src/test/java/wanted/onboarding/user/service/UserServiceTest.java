package wanted.onboarding.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wanted.onboarding.exception.CustomException;

@SpringBootTest
class UserServiceTest {

    private final UserService userService;

    @Autowired
    UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void 이메일_중복_실패() {
        //given
        String email = "dlrlxo999@naver.com";   // DB에 이미 저장되어있는 이메일

        //when
        try {
            userService.isDuplicateEmail(email);
        } catch (CustomException e) {
            //then
            Assertions.assertEquals("DUPLICATE_EMAIL", String.valueOf(e.getErrorCode()));
        }
    }

    @Test
    void 이메일_중복_성공() {
        //given
        String email = "asdf@naver.com";   // DB에 저장되어있지 않은 이메일

        //when
        try {
            userService.isDuplicateEmail(email);
        } catch (CustomException e) {
            //then
            Assertions.fail();
        }
    }

    @Test
    void 이메일_조건_실패() {
        //given
        String email = "dlrlxo999"; // 조건에 맞지 않는 이메일 (@ 없음)

        //when
        try {
            userService.isValidEmail(email);
        } catch (CustomException e) {
            //then
            Assertions.assertEquals("INVALID_EMAIL", String.valueOf(e.getErrorCode()));
        }
    }

    @Test
    void 이메일_조건_성공() {
        //given
        String email = "dlrlxo999@naver.com";   // 조건에 맞는 이메일

        //when
        try {
            userService.isValidEmail(email);
        } catch (CustomException e) {
            //then
            Assertions.fail();
        }
    }

    @Test
    void 비밀번호_조건_실패() {
        //given
        String password = "1234567";    // 조건에 맞지 않는 비밀번호 (8글자 미만)

        //when
        try {
            userService.isValidPassword(password);
        } catch (CustomException e) {
            //then
            Assertions.assertEquals("INVALID_PASSWORD", String.valueOf(e.getErrorCode()));
        }
    }

    @Test
    void 비밀번호_조건_성공() {
        //given
        String password = "12345678";   // 조건에 맞는 비밀번호

        //when
        try {
            userService.isValidPassword(password);
        } catch (CustomException e) {
            //then
            Assertions.fail();
        }
    }
}