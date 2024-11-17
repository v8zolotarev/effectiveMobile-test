package com.example.effectiveMobile_test.mappers;

import com.example.effectiveMobile_test.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    com.example.effectiveMobile_test.entity.User userDTOToUser(UserDTO userDTO);
}
