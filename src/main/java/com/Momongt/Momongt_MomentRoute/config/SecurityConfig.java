package com.Momongt.Momongt_MomentRoute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // POST/PUT 요청 테스트용으로 CSRF 비활성화
                .cors(cors -> cors.disable()) // 필요하면 활성화 가능
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 모든 요청 허용
                )
                .formLogin(form -> form.disable()) // 로그인 폼 사용 안 함
                .httpBasic(httpBasic -> httpBasic.disable()); // Basic 인증 끔

        return http.build();
    }

    // 비밀번호 암호화를 위해 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
