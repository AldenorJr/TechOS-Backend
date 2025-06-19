package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.User;
import br.com.tech.os.ostech.model.dto.authenticationDTO.AuthenticationDTO;
import br.com.tech.os.ostech.model.dto.authenticationDTO.AuthenticationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        tokenService = mock(TokenService.class);
        authenticationController = new AuthenticationController(authenticationManager, tokenService);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        AuthenticationDTO dto = new AuthenticationDTO("user@example.com", "password");
        User user = new User();
        user.setEmail("user@example.com");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("mocked-token");

        ResponseEntity<AuthenticationResponseDTO> response = authenticationController.login(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("mocked-token", response.getBody().token());
    }

}