package com.openclassrooms.starterjwt.payload.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestJacksonTest {

    private final ObjectMapper om = new ObjectMapper();

    @Test
    void serialize_deserialize_signupRequest() throws Exception {
        SignupRequest src = new SignupRequest();
        src.setEmail("alice@example.com");
        src.setPassword("pwd!@#");
        src.setFirstName("Alice");
        src.setLastName("Wonder");

        String json = om.writeValueAsString(src);
        assertThat(json).contains("alice@example.com", "Alice", "Wonder");

        SignupRequest back = om.readValue(json, SignupRequest.class);
        assertThat(back.getEmail()).isEqualTo("alice@example.com");
        assertThat(back.getPassword()).isEqualTo("pwd!@#");
        assertThat(back.getFirstName()).isEqualTo("Alice");
        assertThat(back.getLastName()).isEqualTo("Wonder");
    }
}
