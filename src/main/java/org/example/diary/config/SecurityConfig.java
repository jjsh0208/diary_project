package org.example.diary.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //이 설정을 통해 웹 사이트의 모든 URL을 보안 처리하겠다는 것을 의미
public class SecurityConfig{

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {


        //authorizeHttpRequests: 웹 사이트에 들어오는 모든 요청을 허용하겠다는 뜻입니다.
        //requestMatchers(new AntPathRequestMatcher("/")).permitAll()**: 모든 URL (/**)에 대해 접근을 허용합니다.
        //formLogin: 로그인 페이지와 성공 후 이동할 페이지를 설정합니다.
        //loginPage("/user/login"): 사용자가 로그인할 때 보여줄 페이지입니다.
        //defaultSuccessUrl("/"): 로그인 성공 후 이동할 페이지입니다.
        //logout: 로그아웃과 관련된 설정입니다.
        //logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")): 사용자가 로그아웃할 때 사용되는 URL입니다.
        //logoutSuccessUrl("/"): 로그아웃 성공 후 이동할 페이지입니다.
        //invalidateHttpSession(true): 로그아웃 시 세션을 무효화하여 사용자가 다시 로그인해야 합니다.
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/user/**").permitAll() // /user/** 경로는 인증 없이 접근 가능
                        .requestMatchers("/**").authenticated()) // 나머지 모든 경로는 인증이 필요함
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 커스텀 로그인 페이지 설정
                        .loginProcessingUrl("/user/login") // 사용자 이름과 비밀번호를 제출할 URL
                        .defaultSuccessUrl("/index") // 로그인 성공 후 리디렉션할 페이지
                        .permitAll()) // 모든 사용자가 로그인 페이지에 접근할 수 있도록 허용
                .logout((logout) -> logout
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
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}