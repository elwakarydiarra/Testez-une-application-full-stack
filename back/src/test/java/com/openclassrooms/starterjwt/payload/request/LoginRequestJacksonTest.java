package com.openclassrooms.starterjwt.payload.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestJacksonTest {

    private final ObjectMapper om = new ObjectMapper();

    @Test
    void serialize_deserialize_loginRequest() throws Exception {
        LoginRequest src = new LoginRequest();
        src.setEmail("jack@example.com");
        src.setPassword("pwd123");

        String json = om.writeValueAsString(src);
        assertThat(json).contains("jack@example.com");

        LoginRequest back = om.readValue(json, LoginRequest.class);
        assertThat(back.getEmail()).isEqualTo("jack@example.com");
        assertThat(back.getPassword()).isEqualTo("pwd123");
    }
}
