package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.exception.InvalidUserEmailAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidUserFieldException;
import br.com.tech.os.ostech.exception.InvalidUserIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.User;
import br.com.tech.os.ostech.model.dto.userDTO.UserCreateDTO;
import br.com.tech.os.ostech.model.dto.userDTO.UserUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void testGetCurrentUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void testUpdateCurrentUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserUpdateDTO updateDTO = new UserUpdateDTO("Novo Nome", "novo.email@example.com", "novaSenha");

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setName("Novo Nome");
        updatedUser.setEmail("novo.email@example.com");

        Mockito.when(userService.updateUser(eq("1"), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Novo Nome"))
                .andExpect(jsonPath("$.email").value("novo.email@example.com"));
    }

    @Test
    void testCreateUserThrowsInvalidUserEmailAlreadyExistsException() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "John Doe",
                "existing.email@example.com",
                "password123"
        );

        Mockito.when(userService.createUser(any(UserCreateDTO.class)))
                .thenThrow(new InvalidUserEmailAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void testGetUserByIdThrowsInvalidUserIdException() throws Exception {
        String invalidUserId = "999";

        Mockito.when(userService.getUserById(invalidUserId))
                .thenThrow(new InvalidUserIdException("User not found"));

        mockMvc.perform(get("/users/" + invalidUserId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testCreateUserThrowsInvalidUserFieldException() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "Valid Name",
                "invalid.email@example.com",
                "password123"
        );

        Mockito.when(userService.createUser(any(UserCreateDTO.class)))
                .thenThrow(new InvalidUserFieldException("Invalid user field"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid user field"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "password123"
        );

        User user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Mockito.when(userService.createUser(any(UserCreateDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                "John Doe",
                "john.doe@example.com",
                "password123"
        );

        User user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Mockito.when(userService.updateUser(eq("1"), any(UserUpdateDTO.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Mockito.when(userService.getUserById("1")).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}