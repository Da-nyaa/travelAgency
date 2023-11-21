package com.project.travelAgency;

import com.project.travelAgency.dto.UserDto;
import com.project.travelAgency.model.entity.Role;
import com.project.travelAgency.model.entity.User;
import com.project.travelAgency.model.repository.UserRepository;
import com.project.travelAgency.service.UserService;
import com.project.travelAgency.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService = new UserServiceImpl(userRepository, passwordEncoder);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void testSaveUserDtoSuccess() {
        UserDto userDto = UserDto.builder()
                .username("testUser")
                .password("password")
                .matchingPassword("password")
                .email("test@example.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(new User());

        boolean result = userService.save(userDto);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSaveUserDtoPasswordMismatch() {
        UserDto userDto = UserDto.builder()
                .username("testUser")
                .password("password")
                .matchingPassword("wrongPassword")
                .email("test@example.com")
                .build();

        assertThrows(RuntimeException.class, () -> userService.save(userDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> result = userService.getAll();

        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserByName() {
        String username = "testUser";
        User expectedUser = new User();
        when(userRepository.findFirstByName(username)).thenReturn(expectedUser);

        User result = userService.findByName(username);

        assertEquals(expectedUser, result);
        verify(userRepository, times(1)).findFirstByName(username);
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        String username = "testUser";
        User user = User.builder()
                .name(username)
                .password("password")
                .role(Role.CLIENT)
                .build();

        when(userRepository.findFirstByName(username)).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getRole().name(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        String username = "nonexistentUser";
        when(userRepository.findFirstByName(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }
}
