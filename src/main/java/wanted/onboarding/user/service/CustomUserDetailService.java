package wanted.onboarding.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wanted.onboarding.user.domain.PrincipalDetails;
import wanted.onboarding.user.domain.User;
import wanted.onboarding.user.repository.UserRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()) {
            return new PrincipalDetails(user.get());
        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
    }
}