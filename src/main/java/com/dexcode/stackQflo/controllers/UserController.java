package com.dexcode.stackQflo.controllers;

import com.dexcode.stackQflo.dto.UserDTO;
import com.dexcode.stackQflo.services.UserService;
import com.dexcode.stackQflo.validations.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated({ValidationGroups.Create.class, Default.class}) @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser((userDTO));
        URI resourceUri = URI.create(String.format("/api/users/%s", createdUser.getUserId()));
        return ResponseEntity.created(resourceUri).body(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Validated({ValidationGroups.Update.class, Default.class}) @RequestBody UserDTO userDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.updateUser(userDTO, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
