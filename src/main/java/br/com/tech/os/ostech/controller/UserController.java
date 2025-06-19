package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.model.User;
import br.com.tech.os.ostech.model.dto.userDTO.UserCreateDTO;
import br.com.tech.os.ostech.model.dto.userDTO.UserUpdateDTO;
import br.com.tech.os.ostech.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        log.info("Creating user {}", userCreateDTO);
        User user = userService.createUser(userCreateDTO);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        log.info("Fetching current user: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Updating user {}", userUpdateDTO);
        User user = userService.updateUser(id, userUpdateDTO);
        log.info("User updated successfully: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Updating current user {}", userUpdateDTO);
        User updatedUser = userService.updateUser(user.getId(), userUpdateDTO);
        log.info("Current user updated successfully: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Fetching user with id {}", id);
        User user = userService.getUserById(id);
        log.info("User fetched successfully: {}", user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Deleting user with id {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully");
        return ResponseEntity.noContent().build();
    }



}
