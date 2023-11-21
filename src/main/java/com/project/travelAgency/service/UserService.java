package com.project.travelAgency.service;

import com.project.travelAgency.dto.UserDto;
import com.project.travelAgency.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {

    boolean save(UserDto userDTO);

    void save(User user);

    List<UserDto> getAll();

    User findByName(String name);

    User getDiscount(String name);


}
