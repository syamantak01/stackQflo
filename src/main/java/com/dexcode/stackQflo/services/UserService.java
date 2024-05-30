package com.dexcode.stackQflo.services;

import com.dexcode.stackQflo.dto.UserDTO;


import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, Long userId);

    void deleteUser(Long userId);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

}
