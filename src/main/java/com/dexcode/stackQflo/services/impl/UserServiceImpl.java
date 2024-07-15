package com.dexcode.stackQflo.services.impl;

import com.dexcode.stackQflo.dto.UserDTO;
import com.dexcode.stackQflo.entities.User;
import com.dexcode.stackQflo.exceptions.ResourceNotFoundException;
import com.dexcode.stackQflo.repositories.RoleRepository;
import com.dexcode.stackQflo.repositories.UserRepository;
import com.dexcode.stackQflo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            if(userDTO.getUsername() != null && !userDTO.getUsername().isBlank()){
                existingUser.setUsername(userDTO.getUsername());
            }
            if(userDTO.getEmail() != null && !userDTO.getEmail().isBlank()){
                existingUser.setEmail(userDTO.getEmail());
            }
            if(userDTO.getPassword() != null && !userDTO.getPassword().isBlank()){
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            if(userDTO.getAbout() != null){
                existingUser.setAbout(userDTO.getAbout());
            }
            if(userDTO.getRoleId() != null){
                roleRepository.findById(userDTO.getRoleId())
                        .ifPresentOrElse(existingUser::setRole, () -> {
                            throw new ResourceNotFoundException("Role", "roleId", userDTO.getRoleId());
                        });
            }

            User updatedUser = userRepository.save(existingUser);
            return convertToDTO(updatedUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "userId", userId);
        }
        userRepository.deleteById(userId);

    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {

//        UserDTO userDTO = new UserDTO();
//        userDTO.setUserId(user.getUserId());
//        userDTO.setUsername(user.getUsername());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setAbout(user.getAbout());
//
//        Long roleId = user.getRole().getRoleId();
//
//        userDTO.setRoleId(user.getRole().getRoleId());
//
//        return userDTO;

        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
//        User user = new User();
//        user.setUserId(userDTO.getUserId());
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword(userDTO.getPassword());
//        user.setAbout(userDTO.getAbout());
//
//        Optional<Role> role = roleRepository.findById(userDTO.getRoleId());
//        role.ifPresentOrElse(user::setRole, );
//
//        return user;

        User user =  modelMapper.map(userDTO, User.class);
        roleRepository.findById(userDTO.getRoleId())
                .ifPresentOrElse(user::setRole, () -> {
                    throw new ResourceNotFoundException("Role", "roleId", userDTO.getRoleId());
        });

        return user;
    }
}
