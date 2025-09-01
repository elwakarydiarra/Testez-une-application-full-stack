package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthTokenFilterMoreTest {

    @AfterEach
    void clean() {
        SecurityContextHolder.clearContext();
    }

    private AuthTokenFilter filterWith(JwtUtils jwt, UserDetailsServiceImpl uds) {
        AuthTokenFilter f = new AuthTokenFilter();
        ReflectionTestUtils.setField(f, "jwtUtils", jwt);
        ReflectionTestUtils.setField(f, "userDetailsService", uds);
        return f;
    }

    @Test
    void noAuthorizationHeader_skips() throws Exception {
        JwtUtils jwt = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter f = filterWith(jwt, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        f.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void wrongPrefix_skips() throws Exception {
        JwtUtils jwt = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter f = filterWith(jwt, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Token abc");

        f.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void bearerButEmptyToken_skips() throws Exception {
        JwtUtils jwt = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter f = filterWith(jwt, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer ");

     
        when(jwt.validateJwtToken(anyString())).thenReturn(false);

        f.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void invalidToken_validateFalse_skips() throws Exception {
        JwtUtils jwt = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter f = filterWith(jwt, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer bad.jwt.token");
        when(jwt.validateJwtToken("bad.jwt.token")).thenReturn(false);

        f.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
