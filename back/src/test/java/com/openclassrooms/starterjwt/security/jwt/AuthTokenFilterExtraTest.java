package com.openclassrooms.starterjwt.security.jwt;

import javax.servlet.FilterChain;                  
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import static org.mockito.Mockito.*;


class AuthTokenFilterExtraTest {

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenNoJwtToken() throws Exception {
        
        AuthTokenFilter filter = new AuthTokenFilter();
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        
        filter.doFilterInternal(request, response, filterChain);

        
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoMoreInteractions(filterChain);
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenJwtInvalid() throws Exception {
        
        AuthTokenFilter filter = new AuthTokenFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid.token.here");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);

       
        ReflectionTestUtils.setField(filter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);

        when(jwtUtils.validateJwtToken("invalid.token.here")).thenReturn(false);

       
        filter.doFilterInternal(request, response, filterChain);

       
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtils, times(1)).validateJwtToken("invalid.token.here");
        
        verifyNoInteractions(userDetailsService);
        verifyNoMoreInteractions(filterChain);
    }
}
