package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;


class JwtUtilsExtraTest {

    private static UserDetailsImpl user(String email) {
        return UserDetailsImpl.builder()
                .id(1L)
                .username(email)
                .firstName("Alice")
                .lastName("Doe")
                .admin(false)
                .password("pwd")
                .build();
    }

    @Test
    void generate_and_parse_username_ok() {
        JwtUtils jwt = new JwtUtils();

        
        String secret = "test-secret-32-bytes-minimum-test-secret-32";
        ReflectionTestUtils.setField(jwt, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 60_000);

        UserDetailsImpl principal = user("alice@test.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        String token = jwt.generateJwtToken(auth);

        // Sanity checks
        assertThat(token).isNotBlank();
        assertThat(jwt.validateJwtToken(token)).isTrue();
        assertThat(jwt.getUserNameFromJwtToken(token)).isEqualTo("alice@test.com");
    }

    @Test
    void validateJwtToken_returnsFalse_whenExpired() throws Exception {
        JwtUtils jwt = new JwtUtils();

        String secret = "test-secret-32-bytes-minimum-test-secret-32";
        ReflectionTestUtils.setField(jwt, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 1); 

        UserDetailsImpl principal = user("exp@test.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        String token = jwt.generateJwtToken(auth);

       
        Thread.sleep(2);

        assertThat(jwt.validateJwtToken(token)).isFalse();
    }

    @Test
    void validateJwtToken_returnsFalse_whenMalformed() {
        JwtUtils jwt = new JwtUtils();

        String secret = "test-secret-32-bytes-minimum-test-secret-32";
        ReflectionTestUtils.setField(jwt, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 60_000);

        
        String malformed = "this.is.not.a.jwt";

        assertThat(jwt.validateJwtToken(malformed)).isFalse();
    }
    @Test
    void validateJwtToken_returnsFalse_whenEmptyOrUnsupported() {
        JwtUtils jwt = new JwtUtils();
        ReflectionTestUtils.setField(jwt, "jwtSecret", "test-secret-32-bytes-minimum-test-secret-32");
        ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 60000);

        
        assertThat(jwt.validateJwtToken("")).isFalse();

        
        String unsupported = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ0ZXN0In0.";
        assertThat(jwt.validateJwtToken(unsupported)).isFalse();
    }
    
    

}
