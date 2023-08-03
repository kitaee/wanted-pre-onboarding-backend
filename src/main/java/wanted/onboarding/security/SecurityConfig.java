package wanted.onboarding.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wanted.onboarding.user.repository.UserRepository;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * 인증 불필요 URL
     * */
    private static final String[] PERMIT_URL_ARRAY={
            "/","/**/*.png","/**/*.jpg","/**/*.js","/**/*.css","/**/*.html","/**/*.gif","/**/*.svg",
            "/api/user", "/login"
    };

    /**
     * 인증 필요 URL
     * */
    private static final String[] AUTHENTICATED_URL_ARRAY={
            "/api/board", "/api/board/**"
    };


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilter(corsConfig.corsFilter())
                .authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .antMatchers(AUTHENTICATED_URL_ARRAY).authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtTokenProvider))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
