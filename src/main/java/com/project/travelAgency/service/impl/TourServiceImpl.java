package com.project.travelAgency.service.impl;

import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.model.entity.Cart;
import com.project.travelAgency.model.entity.Country;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.entity.User;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.CartService;
import com.project.travelAgency.service.TourService;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    final static Logger logger = Logger.getLogger(TourServiceImpl.class);
    private final TourRepository tourRepository;
    private final UserService userService;
    private final CartService cartService;



    @Override
    public List<TourDto> getAll() {
        logger.info("Get TourDtoList");
        return tourRepository.findAll().stream()
                .map(TourDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addToUserCart(Long tourId, String username) {
        logger.info("Find User by name");
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User " + username + " not found");
        }
        logger.info("Get Cart for User");
        Cart cart = user.getCart();
        if (cart == null) {
            Cart newCart = cartService.createCart(user, Collections.singletonList(tourId));
            user.setCart(newCart);
            userService.save(user);
        } else {
            cartService.addTours(cart, Collections.singletonList(tourId));
        }
    }

    @Override
    public Tour getById(long id) {

        return tourRepository.getById(id);
    }

    @Override
    public boolean save(TourDto tourDto) {

        logger.info("Conversion TourDto into Tour and save TOur in DB");
        Tour tour = Tour.builder()
                .typeOfTour(tourDto.getTypeOfTour())
                .country(new Country(tourDto.getCountryDto()))
                .days(tourDto.getDays())
                .price(tourDto.getPrice())
                .status(tourDto.getStatus())
                .build();

        tourRepository.save(tour);
        return true;
    }
}

