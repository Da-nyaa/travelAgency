package com.project.travelAgency;

import com.project.travelAgency.controller.UserController;
import com.project.travelAgency.dto.UserDto;
import com.project.travelAgency.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUserList() {
        List<UserDto> userList = new ArrayList<>();
        userList.add(new UserDto());
        userList.add(new UserDto());

        when(userService.getAll()).thenReturn(userList);

        String result = userController.userList(model);

        assertEquals("userList", result);
        verify(model, times(1)).addAttribute("users", userList);
        verify(userService, times(1)).getAll();
    }

    @Test
    void testNewUser() {
        String result = userController.newUser(model);

        assertEquals("user", result);
        verify(model, times(1)).addAttribute("user", new UserDto());
    }

    @Test
    void testSaveUserWithValidData() {
        UserDto userDto = new UserDto();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.save(userDto)).thenReturn(true);

        String result = userController.saveUser(userDto, bindingResult, model);

        assertEquals("redirect:/users", result);
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).save(userDto);
    }

    @Test
    void testSaveUserWithInvalidData() {
        UserDto userDto = new UserDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userController.saveUser(userDto, bindingResult, model);

        assertEquals("error", result);
        verify(bindingResult, times(1)).hasErrors();
    }
}
