package com.project.travelAgency.service.impl;

import com.project.travelAgency.dto.UserDto;
import com.project.travelAgency.model.entity.Order;
import com.project.travelAgency.model.entity.Role;
import com.project.travelAgency.model.entity.User;
import com.project.travelAgency.model.repository.UserRepository;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    final static Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean save(UserDto userDto) {

        logger.info("Conversion UserDto in User and then save User");

        if (!Objects.equals(userDto.getPassword(), userDto.getMatchingPassword())) {
            throw new RuntimeException("Password is not equal");
        }
        User user = User.builder()
                .name(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAll() {

        logger.info("Conversion UserList in UserDtoList");

        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findByName(String name) {
        return userRepository.findFirstByName(name);
    }

    @Override
    public User getDiscount(String name) {

        logger.info("Get a discount for user");

        User user = userRepository.findFirstByName(name);

        List<Order> orders = user.getOrders();
        BigDecimal newSum = orders.stream().map(Order::getSum).reduce(BigDecimal.valueOf(0), BigDecimal::add);

        if (newSum.compareTo(new BigDecimal(1000)) <= 0) {
            user.setDiscount(BigDecimal.valueOf(0));
        }
        if (newSum.compareTo(new BigDecimal(1000)) >= 0 && newSum.compareTo(new BigDecimal(2000)) <= 0) {
            user.setDiscount(BigDecimal.valueOf(5));
        }
        if (newSum.compareTo(new BigDecimal(2000)) >= 0 && newSum.compareTo(new BigDecimal(3000)) <= 0) {
            user.setDiscount(BigDecimal.valueOf(10));
        }
        if (newSum.compareTo(new BigDecimal(3000)) >= 0 && newSum.compareTo(new BigDecimal(4000)) <= 0) {
            user.setDiscount(BigDecimal.valueOf(15));
        }
        if (newSum.compareTo(new BigDecimal(4000)) >= 0) {
            user.setDiscount(BigDecimal.valueOf(20));
        } else {

            logger.info("Save User with discount");
            userRepository.save(user);
        }
        logger.info("Save User with discount");
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                roles);
    }

    private UserDto toDto(User user) {

        logger.info("Conversion User to UserDto");

        return UserDto.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }
}
