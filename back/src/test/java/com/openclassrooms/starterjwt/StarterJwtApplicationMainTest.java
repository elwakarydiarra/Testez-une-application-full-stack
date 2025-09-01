package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SpringBootSecurityJwtApplicationMainTest {

    @Test
    @DisplayName("main() démarre en profil test, sans autoconfig Spring Security")
    void main_startsApplication() {
        assertDoesNotThrow(() ->
            SpringBootSecurityJwtApplication.main(new String[] {
                "--spring.profiles.active=test",
                "--spring.main.web-application-type=servlet",
                "--spring.autoconfigure.exclude=" +
                    "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                    "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"
            })
        );
    }
}
