package wanted.onboarding.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.exception.CustomException;
import wanted.onboarding.exception.ErrorCode;
import wanted.onboarding.user.domain.User;
import wanted.onboarding.user.dto.JoinResponse;
import wanted.onboarding.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    /**
     *  Date : 2023-08-01
     *  Description: 회원가입 전체 메소드
     * */
    @Transactional
    public JoinResponse join(String email, String password) {
        isDuplicateEmail(email);    // 이메일 중복 검사
        isValidEmail(email);    // 이메일 조건 검사
        isValidPassword(password);  // 비밀번호 조건 검사

        User user = User.builder()
                .email(email)
                .password(encoder.encode(password))
                .build();

        save(user); // 유저 정보 저장

        return JoinResponse.builder()
                .userId(user.getUserId())
                .build();
    }

    /**
     *  Date : 2023-08-01
     *  Description: 이메일 중복체크 메소드
     * */
    public void isDuplicateEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    /**
     *  Date : 2023-08-01
     *  Description: 유저 저장 메소드
     * */
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     *  Date : 2023-08-01
     *  Description: 이메일 조건 검사 메소드
     * */
    public void isValidEmail(String email) {
        if(!email.contains("@")) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }
    }

    /**
     *  Date : 2023-08-01
     *  Description: 비밀번호 조건 검사 메소드
     * */
    public void isValidPassword(String password) {
        if(password.length() < 8) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    /**
     *  Date : 2023-08-03
     *  Description: username(email)로 사용자 찾기 메소드
     * */
    public User getUserEntityByLogin(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
