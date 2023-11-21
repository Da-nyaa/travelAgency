package com.project.travelAgency;

import com.project.travelAgency.dto.CountryDto;
import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.model.entity.*;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.CartService;
import com.project.travelAgency.service.UserService;
import com.project.travelAgency.service.impl.TourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TourServiceImplTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private TourServiceImpl tourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddToUserCart() {
        Long tourId = 1L;
        String username = "testUser";
        User user = new User();
        Tour tour = new Tour();
        when(userService.findByName(username)).thenReturn(user);
        when(tourRepository.getById(tourId)).thenReturn(tour);
        when(cartService.createCart(user, Collections.singletonList(tourId))).thenReturn(new Cart());

        tourService.addToUserCart(tourId, username);

        verify(userService, times(1)).findByName(username);
        verify(cartService, times(1)).createCart(user, Collections.singletonList(tourId));
        verify(cartService, never()).addTours(any(), any());
        verify(userService, times(1)).save(user);
    }

    @Test
    void testAddToUserCartExistingCart() {
        Long tourId = 1L;
        String username = "testUser";
        User user = new User();
        Tour tour = new Tour();
        Cart existingCart = new Cart();
        user.setCart(existingCart);

        when(userService.findByName(username)).thenReturn(user);
        when(tourRepository.getById(tourId)).thenReturn(tour);

        tourService.addToUserCart(tourId, username);

        verify(userService, times(1)).findByName(username);
        verify(cartService, times(1)).addTours(existingCart, Collections.singletonList(tourId));
        verify(cartService, never()).createCart(any(), any());
        verify(userService, never()).save(user);
    }

    @Test
    void testGetById() {
        long tourId = 1L;
        Tour expectedTour = new Tour();
        when(tourRepository.getById(tourId)).thenReturn(expectedTour);

        Tour result = tourService.getById(tourId);

        assertEquals(expectedTour, result);
    }

    @Test
    void testSave() {
        TourDto tourDto = TourDto.builder()
                .typeOfTour(TypeOfTour.Shopping)
                .countryDto(new CountryDto())
                .days(5)
                .price(BigDecimal.valueOf(500))
                .status(Status.ACTIVE)
                .build();

        boolean result = tourService.save(tourDto);

        verify(tourRepository, times(1)).save(any());
        assertEquals(true, result);
    }
}
