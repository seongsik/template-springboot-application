package com.sik.template.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sik.template.biz.api.base.response.RestApiExceptionResponse;
import com.sik.template.common.exception.ExceptionCode;
import com.sik.template.common.security.JwtAuthenticationFilter;
import com.sik.template.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/swagger-ui/**"
                        , "/h2-console/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/api/account/signin").permitAll()
                    .antMatchers("/api/**").hasAuthority("API_CALL")


                .and()
                .httpBasic().disable() // Rest API 의 경우 기본설정 사용 안함.
                .formLogin().disable()
                .cors().disable()
                .csrf()
                    .ignoringAntMatchers("/h2-console/**")
                    .disable() // Rest API 의 경우 CSRF 보안 불필요
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT Token 인증의 경우 생성 안함

                .and()
                .exceptionHandling()
                .authenticationEntryPoint((((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getOutputStream(), RestApiExceptionResponse.builder()
                            .errorCode(ExceptionCode.UNAUTHORIZED.getCode())
                            .errorMessage("UNAUTHORIZED")
                            .build());

                })))
                .accessDeniedHandler((((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getOutputStream(), RestApiExceptionResponse.builder()
                            .errorCode(ExceptionCode.FORBIDDEN.getCode())
                            .errorMessage("FORBIDDEN")
                            .build());
                })))

                .and()
                .headers().frameOptions().disable()

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                .build();

    }
}
