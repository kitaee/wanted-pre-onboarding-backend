package wanted.onboarding.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.onboarding.user.dto.JoinRequest;
import wanted.onboarding.user.dto.JoinResponse;
import wanted.onboarding.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinRequest joinRequest) {
        return new ResponseEntity<>(userService.join(joinRequest.getEmail(), joinRequest.getPassword()), HttpStatus.CREATED);
    }
}
