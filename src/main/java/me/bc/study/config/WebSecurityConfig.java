package me.bc.study.config;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import lombok.RequiredArgsConstructor;
import me.bc.study.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig{

  private final UserDetailService userService;

  // 1. 스프링 시큐리티 기능 비활성화
  @Bean
  public WebSecurityCustomizer configure(){
    return (web -> web.ignoring()
        .requestMatchers(toH2Console())
        .requestMatchers("/static/**"));
  }

  // 2. 특정 HTTP 요청에 대한 웹 기반 보안 구성
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    return http
        .authorizeRequests(auth -> auth  // 3. 인증, 인가 설정
        .requestMatchers("/login", "/signup", "/user")
        .permitAll()
        .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin    // 4. 폼 기반 로그인 설정
        .loginPage("/login")
        .defaultSuccessUrl("/articles"))
        .logout(logout -> logout            // 5. 로그아웃 설정
        .logoutSuccessUrl("/login")     
        .invalidateHttpSession(true))
        .csrf(AbstractHttpConfigurer::disable)  // 6.. csrf 비활성화
        .build();
  }

  // 7. 인증 관리자 관련 설정
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
      UserDetailService userDetailService) throws Exception{
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);
    authProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return new ProviderManager(authProvider);
  }

  // 8. 패스워드 인코더로 사용할 빈 등록
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
