package wanted.onboarding.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wanted.onboarding.exception.ErrorCode;
import wanted.onboarding.exception.ErrorResponseEntity;
import wanted.onboarding.user.domain.PrincipalDetails;
import wanted.onboarding.user.domain.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(!username.contains("@") || password.length() < 8) {
            throw new BadCredentialsException("로그인 조건 불일치");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {

        PrincipalDetails loginUser = (PrincipalDetails) authResult.getPrincipal();
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtTokenProvider.createToken(loginUser.getUsername()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setStatus(HttpStatus.OK.value());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();

        if(!username.contains("@")) {
            out.write(objectMapper.writeValueAsString(ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_EMAIL)));
        } else if (password.length() < 8) {
            out.write(objectMapper.writeValueAsString(ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_PASSWORD)));
        }

        out.flush();
        out.close();
    }
}
