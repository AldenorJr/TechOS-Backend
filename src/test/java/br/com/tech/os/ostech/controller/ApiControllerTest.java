package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
@Import(TestSecurityConfig.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void status_shouldReturnOnline() throws Exception {
        mockMvc.perform(get("/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"));
    }
}