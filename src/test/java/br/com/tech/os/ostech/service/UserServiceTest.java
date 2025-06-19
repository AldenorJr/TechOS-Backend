package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.TestDummies;
import br.com.tech.os.ostech.exception.InvalidUserEmailAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidUserIdException;
import br.com.tech.os.ostech.model.User;
import br.com.tech.os.ostech.model.dto.userDTO.UserCreateDTO;
import br.com.tech.os.ostech.model.dto.userDTO.UserUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário com e-mail já existente em outro usuário")
    void updateUserShouldThrowExceptionWhenEmailAlreadyExists() {
        String userId = "123";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Novo Nome", "email@existente.com", "novaSenha");
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Nome Antigo");
        existingUser.setEmail("email@antigo.com");
        existingUser.setPassword("senhaAntiga");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(userUpdateDTO.email())).thenReturn(true);

        assertThrows(InvalidUserEmailAlreadyExistsException.class, () -> userService.updateUser(userId, userUpdateDTO));

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(userUpdateDTO.email());
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void createUserShouldSucessy() {
        UserCreateDTO userCreateDTO = TestDummies.createUserCreateDTO();
        User userMock = new User();
        userMock.setName(userCreateDTO.name());
        userMock.setEmail(userCreateDTO.email());
        userMock.setPassword(userCreateDTO.password());

        when(userRepository.existsByEmail(userCreateDTO.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userMock);

        User user = userService.createUser(userCreateDTO);

        assertEquals(userCreateDTO.name(), user.getName());
        assertEquals(userCreateDTO.email(), user.getEmail());
        assertEquals(userCreateDTO.password(), user.getPassword());

        verify(userRepository).existsByEmail(userCreateDTO.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    void updateUserShouldSucceed() {
        String userId = "123";
        UserUpdateDTO userUpdateDTO = TestDummies.createUserUpdateDTO();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("oldemail@example.com");
        existingUser.setPassword("oldpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, userUpdateDTO);

        assertEquals(userUpdateDTO.name(), updatedUser.getName());
        assertEquals(userUpdateDTO.email(), updatedUser.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar um usuário inexistente")
    void updateUserShouldThrowExceptionWhenUserNotFound() {
        String userId = "123";
        UserUpdateDTO userUpdateDTO = TestDummies.createUserUpdateDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidUserIdException.class, () -> userService.updateUser(userId, userUpdateDTO));

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um usuário com e-mail já existente")
    void createUserShouldThrowExceptionWhenEmailAlreadyExists() {
        UserCreateDTO userCreateDTO = TestDummies.createUserCreateDTO();

        when(userRepository.existsByEmail(userCreateDTO.email())).thenReturn(true);

        assertThrows(InvalidUserEmailAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));

        verify(userRepository).existsByEmail(userCreateDTO.email());
    }

    @Test
    @DisplayName("Deve buscar um usuário por ID com sucesso")
    void getUserByIdShouldSucceed() {
        String userId = "123";
        User userMock = new User();
        userMock.setId(userId);
        userMock.setName("Test User");
        userMock.setEmail("test@example.com");
        userMock.setPassword("password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));

        User user = userService.getUserById(userId);

        assertEquals(userId, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um usuário inexistente por ID")
    void getUserByIdShouldThrowExceptionWhenUserNotFound() {
        String userId = "123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidUserIdException.class, () -> userService.getUserById(userId));

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deleteUserShouldSucceed() {
        String userId = "123";

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar um usuário inexistente")
    void deleteUserShouldThrowExceptionWhenUserNotFound() {
        String userId = "123";

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(InvalidUserIdException.class, () -> userService.deleteUser(userId));

        verify(userRepository).existsById(userId);
    }

}