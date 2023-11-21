package com.project.travelAgency;

import com.project.travelAgency.controller.TourController;
import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.model.entity.Country;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.impl.TourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TourControllerTest {

    @Mock
    private TourServiceImpl tourService;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private TourController tourController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testList() {
        List<TourDto> expectedList = new ArrayList<>();
        when(tourService.getAll()).thenReturn(expectedList);

        String result = tourController.list(model);

        verify(model, times(1)).addAttribute(eq("tours"), anyList());
        assertEquals("tours", result);
    }

    @Test
    void testAddCart() {
        Long tourId = 1L;
        when(principal.getName()).thenReturn("testUser");

        String result = tourController.addCart(tourId, principal);

        verify(tourService, times(1)).addToUserCart(tourId, "testUser");
        assertEquals("redirect:/tours", result);
    }

    @Test
    void testShowCreateTour() {
        String result = tourController.showCreateTour(model);

        verify(model, times(1)).addAttribute(eq("tour"), any(TourDto.class));
        assertEquals("createTour", result);
    }

    @Test
    void testCreateTour() {
        TourDto tourDto = new TourDto();
        BindingResult bindingResult = mock(BindingResult.class);

        String result = tourController.createTour(tourDto, bindingResult, model);

        verify(tourService, times(1)).save(tourDto);
        verify(model, times(1)).addAttribute(eq("tours"), anyList());
        assertEquals("tours", result);
    }

    @Test
    void testDeleteTour() {
        Long tourId = 1L;

        String result = tourController.deleteTour(tourId, model);

        verify(tourRepository, times(1)).deleteById(tourId);
        verify(model, times(1)).addAttribute(eq("tours"), anyList());
        assertEquals("tours", result);
    }


    @Test
    void testEditTourPost() {
        Tour tour = new Tour();
        Country country = new Country();

        String result = tourController.editTour(tour, country, model);

        verify(tourRepository, times(1)).save(tour);
        verify(model, times(1)).addAttribute(eq("country"), any(Country.class));
        verify(model, times(1)).addAttribute(eq("tours"), anyList());
        assertEquals("tours", result);
    }
}
