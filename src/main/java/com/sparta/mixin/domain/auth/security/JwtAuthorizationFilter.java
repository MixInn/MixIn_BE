package com.sparta.mixin.domain.auth.security;

import com.sparta.mixin.domain.jwt.JwtUtil;
import com.sparta.mixin.domain.user.entity.UserRoleEnum;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info("Attempting authentication for user: {}", req.getHeader(JwtUtil.AUTHORIZATION_HEADER));

        String tokenValue = jwtUtil.getTokenFromRequest(req);
        log.info("Extracted token: {}", tokenValue);

        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token validation failed");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                String username = info.getSubject();
                UserRoleEnum role = jwtUtil.getRoleFromToken(tokenValue);

                setAuthentication(username, role);
                log.info("Authenticated user: {}", username);
            } catch (Exception e) {
                log.error("Error getting user info from token: {}", e.getMessage());
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username, UserRoleEnum role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username, role);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username, UserRoleEnum role) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = getAuthorities(role);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRoleEnum role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

}