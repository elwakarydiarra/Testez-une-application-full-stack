package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    private AuthTokenFilter newFilterWith(JwtUtils jwtUtils, UserDetailsServiceImpl uds) {
        AuthTokenFilter filter = new AuthTokenFilter();
        ReflectionTestUtils.setField(filter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(filter, "userDetailsService", uds);
        return filter;
    }

    @Test
    @DisplayName("Bearer token valide -> authentification posée et chain.doFilter appelé")
    void doFilter_setsAuthentication_whenValidBearerToken() throws ServletException, IOException {
        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter filter = newFilterWith(jwtUtils, uds);

        String token = "jwt-token";
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("alice@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userDetails.getPassword()).thenReturn("pwd");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(uds.loadUserByUsername("alice@example.com")).thenReturn(userDetails);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilterInternal(req, res, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("alice@example.com",
                SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(req, res);
    }

    @Test
    @DisplayName("Sans header Authorization -> pas d’auth et chain.doFilter appelé")
    void doFilter_skipsAuthentication_whenNoHeader() throws ServletException, IOException {
        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter filter = newFilterWith(jwtUtils, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
        verify(uds, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("Header non 'Bearer ' -> pas d’auth")
    void doFilter_skips_whenWrongPrefix() throws ServletException, IOException {
        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter filter = newFilterWith(jwtUtils, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Token abc");

        filter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
        verify(uds, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("Bearer présent mais token vide -> pas d’auth")
    void doFilter_skips_whenEmptyToken() throws ServletException, IOException {
        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsServiceImpl uds = mock(UserDetailsServiceImpl.class);
        AuthTokenFilter filter = newFilterWith(jwtUtils, uds);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer   ");

        filter.doFilterInternal(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
       
        verify(uds, never()).loadUserByUsername(anyString());
    }
}
