package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidUserEmailAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidUserIdException;
import br.com.tech.os.ostech.model.User;
import br.com.tech.os.ostech.model.dto.userDTO.UserCreateDTO;
import br.com.tech.os.ostech.model.dto.userDTO.UserUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User createUser(UserCreateDTO userCreateDTO) {

        String encryptedPassword = new BCryptPasswordEncoder().encode(userCreateDTO.password());

        if (userRepository.existsByEmail(userCreateDTO.email())) {
            log.error("User with email {} already exists", userCreateDTO.email());
            throw new InvalidUserEmailAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setName(userCreateDTO.name());
        user.setEmail(userCreateDTO.email());
        user.setPassword(encryptedPassword);
        user.setId(String.valueOf(UUID.randomUUID()));

        log.info("Creating user {}", user);
        return userRepository.save(user);
    }

    public User updateUser(String id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException("User not found"));

        if(userRepository.existsByEmail(userUpdateDTO.email()) &&
           !user.getEmail().equals(userUpdateDTO.email())) {
            log.error("User with email {} already exists", userUpdateDTO.email());
            throw new InvalidUserEmailAlreadyExistsException("User with this email already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userUpdateDTO.password());

        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());
        user.setPassword(encryptedPassword);

        log.info("Updating user {}", user);
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        log.info("Getting user with id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException("User not found"));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            log.error("User with id {} not found", id);
            throw new InvalidUserIdException("User not found");
        }
        log.info("Deleting user with id {}", id);
        userRepository.deleteById(id);
    }



}
