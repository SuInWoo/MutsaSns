package com.sns.config;

import com.sns.domain.entity.user.User;
import com.sns.service.UserService;
import com.sns.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 권한 주거나 안주거나
        // 개찰구 역할

        // 막아야 할 때
        // 1. 토큰 미소지 --> Request할 때 토큰 미 입력
        // 2. 다른 종류의 토큰을 가져왔을 때
        // 3. 기간이 지난 토큰 소지

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader:{}", authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("헤더를 가져오는 과정에서 에러가 났습니다. 헤더가 null이거나 잘못되었습니다.");
            filterChain.doFilter(request, response);
            return; // 아래에서 문열어주기전에 return을 해서 다 튕겨져 나감
        }

        String token;
        try {
            token = authorizationHeader.split(" ")[1];   // 앞에 'Bearer'만 붙였을 경우도 방지하기 위해
        } catch (Exception e) {
            log.error("token 추출 실패");
            filterChain.doFilter(request, response);
            return; // 아래에서 문열어주기전에 return을 해서 다 튕겨져 나감
        }

        // 토큰 기간 만료
        if (JwtUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Token에서 Claim에서 UserName꺼내기
        String userName = JwtUtil.getUserName(token, secretKey);
        log.info("userName:{}", userName);

        // UserDetail가져오기
        User user = userService.getUserByUserName(userName);
        log.info("userRole:{}", user.getUserRole());

        //문 열어주기, Role 바인딩
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(), null,
                List.of(new SimpleGrantedAuthority(user.getUserRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 권한 부여
        filterChain.doFilter(request, response);
    }
}
