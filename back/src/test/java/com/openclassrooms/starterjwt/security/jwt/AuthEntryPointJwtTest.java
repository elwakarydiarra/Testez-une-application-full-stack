package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthEntryPointJwtTest {

    @Test
    void commence_sets401() throws IOException, ServletException {
        AuthEntryPointJwt entry = new AuthEntryPointJwt();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();

        entry.commence(req, res, new org.springframework.security.core.AuthenticationException("boom") {});

        assertEquals(401, res.getStatus());
        assertTrue(res.getContentAsString().contains("Unauthorized"));
    }

    @Test
    void commence_sets401_even_if_response_had_status_before() throws IOException, ServletException {
        AuthEntryPointJwt entry = new AuthEntryPointJwt();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        res.setStatus(200); 

        entry.commence(req, res, new org.springframework.security.core.AuthenticationException("any") {});

        assertEquals(401, res.getStatus(), "Le EntryPoint doit renvoyer 401 dans tous les cas");
        assertFalse(res.getContentAsString().isEmpty(), "Le corps de réponse ne devrait pas être vide");
    }

    @Test
    void commence_handles_null_message_on_exception() throws IOException, ServletException {
        AuthEntryPointJwt entry = new AuthEntryPointJwt();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();

       
        entry.commence(req, res, new org.springframework.security.core.AuthenticationException(null) {});

        assertEquals(401, res.getStatus());
        
        assertFalse(res.getContentAsString().isEmpty());
    }
}
