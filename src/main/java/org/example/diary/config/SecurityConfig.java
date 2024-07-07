package org.example.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/user/**").permitAll() // /user/** 경로는 인증 없이 접근 가능
                        .requestMatchers("/**").authenticated()) // 나머지 모든 경로는 인증이 필요함
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login") // 커스텀 로그인 페이지 설정
                        .loginProcessingUrl("/user/login") // 사용자 이름과 비밀번호를 제출할 URL
                        .defaultSuccessUrl("/diary/list") // 로그인 성공 후 리디렉션할 페이지
                        .failureUrl("/user/login?error=true") // 로그인 실패 시 리디렉션할 페이지
                        .permitAll()) // 모든 사용자가 로그인 페이지에 접근할 수 있도록 허용
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃할 때 사용할 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리디렉션할 페이지
                        .invalidateHttpSession(true)); // 로그아웃 시 세션 무효화
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/user/login?error=true");
    }
}
