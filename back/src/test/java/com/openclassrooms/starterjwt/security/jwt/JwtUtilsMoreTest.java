package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsMoreTest {
  private final String goodKey = "a-very-long-secret-key-used-for-tests-1234567890";
  private final String badKey  = "another-different-secret-key-0987654321";
  private JwtUtils utils;

  @BeforeEach
  void setUp(){
    utils = new JwtUtils();
    ReflectionTestUtils.setField(utils,"jwtSecret",goodKey);
    ReflectionTestUtils.setField(utils,"jwtExpirationMs",3600000);
  }

  @Test
  void validate_false_whenSignatureInvalid() {
    String forged = Jwts.builder()
        .setSubject("u@x.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+3600000))
        .signWith(SignatureAlgorithm.HS512, badKey) 
        .compact();

    assertThat(utils.validateJwtToken(forged)).isFalse();
  }

  @Test
  void validate_false_whenTokenMalformed() {
    assertThat(utils.validateJwtToken("not.a.jwt")).isFalse();
    assertThat(utils.validateJwtToken("abc")).isFalse();
  }
}
