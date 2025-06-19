package br.com.tech.os.ostech.infra.security;

import br.com.tech.os.ostech.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();
        Field secretField = TokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, "test-secret");
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        User user = new User();
        user.setEmail("user@example.com");

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_shouldReturnSubject_whenTokenIsValid() {
        User user = new User();
        user.setEmail("user@example.com");
        String token = tokenService.generateToken(user);

        String subject = tokenService.validateToken(token);

        assertEquals("user@example.com", subject);
    }

    @Test
    void validateToken_shouldReturnEmpty_whenTokenIsInvalid() {
        String invalidToken = "invalid.token.value";

        String subject = tokenService.validateToken(invalidToken);

        assertEquals("", subject);
    }

    @Test
    void genExpirationDate_shouldReturnFutureInstant() {
        Instant expiration = tokenService.genExpirationDate();
        assertTrue(expiration.isAfter(Instant.now()));
    }
}