package wanted.onboarding.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import wanted.onboarding.user.domain.PrincipalDetails;
import wanted.onboarding.user.domain.User;
import wanted.onboarding.user.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        if(!jwtTokenProvider.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        String username = jwtTokenProvider.getUserEmail(token);

        if(username != null) {
            Optional<User> user = userRepository.findByEmail(username);

            if(user.isEmpty()) {
                chain.doFilter(request, response);
                return;
            }

            PrincipalDetails principalDetails = new PrincipalDetails(user.get());
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
