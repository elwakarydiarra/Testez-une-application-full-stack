package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String secret = "test-secret-which-is-long-enough-for-hmac";
    private final int expirationMs = 3_600_000; 

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", expirationMs);
    }

    private String buildToken(String subject, long expOffsetMs, String signingSecret) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expOffsetMs))
                .signWith(SignatureAlgorithm.HS512, signingSecret)
                .compact();
    }

    private String buildToken(String subject, long expOffsetMs) {
        return buildToken(subject, expOffsetMs, secret);
    }

    @Test
    @DisplayName("parse + validate -> OK avec token valide")
    void parse_and_validate_valid_token() {
        String token = buildToken("alice@example.com", expirationMs);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("alice@example.com", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    @DisplayName("validate -> false avec token non JWT")
    void validate_returnsFalse_whenInvalid() {
        assertFalse(jwtUtils.validateJwtToken("not-a-jwt"));
    }

    @Test
    @DisplayName("validate -> false avec token expiré")
    void validate_returnsFalse_whenExpired() {
        String expired = buildToken("bob@example.com", -1000);
        assertFalse(jwtUtils.validateJwtToken(expired));
    }

    @Test
    @DisplayName("getUserNameFromJwtToken -> exception si token mal formé")
    void getUserNameFromJwtToken_throwsOnInvalidToken() {
        assertThrows(Exception.class, () -> jwtUtils.getUserNameFromJwtToken("!!!bad-token!!!"));
    }

    @Test
    @DisplayName("validate -> false si signature incorrecte")
    void validate_returnsFalse_whenSignatureIsWrong() {
        String badSigToken = buildToken("mallory@example.com", 60_000, "other-secret");
        assertFalse(jwtUtils.validateJwtToken(badSigToken));
    }

    @Test
    @DisplayName("validate -> false si null ou vide")
    void validate_returnsFalse_whenTokenIsNullOrEmpty() {
        assertFalse(jwtUtils.validateJwtToken(null));
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken("   "));
    }
}
