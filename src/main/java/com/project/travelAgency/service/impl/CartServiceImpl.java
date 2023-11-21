package com.project.travelAgency.service.impl;


import com.project.travelAgency.dto.CartDetailDto;
import com.project.travelAgency.dto.CartDto;
import com.project.travelAgency.model.entity.*;
import com.project.travelAgency.model.repository.CartRepository;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.CartService;
import com.project.travelAgency.service.OrderService;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    final static Logger logger = Logger.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;
    private final TourRepository tourRepository;
    private final UserService userService;
    private final OrderService orderService;


    @Override
    public Cart createCart(User user, List<Long> tourIds) {

        logger.info("Create Cart for User");
        Cart cart = new Cart();
        cart.setUser(user);
        List<Tour> tourList = getCollectRefToursByIds(tourIds);
        logger.info("Add tours_ids in Cart and save User`s Cart in DB");
        cart.setTours(tourList);
        return cartRepository.save(cart);
    }

    private List<Tour> getCollectRefToursByIds(List<Long> tourIds) {

        logger.info("Get IdList from Tours");

        return tourIds.stream()
                .map(tourRepository::getOne)
                .collect(Collectors.toList());
    }

    @Override
    public void addTours(Cart cart, List<Long> tourIds) {

        logger.info("Add tours in Cart");

        List<Tour> tours = cart.getTours();
        List<Tour> newTourList = tours == null ? new ArrayList<>() : new ArrayList<>(tours);
        newTourList.addAll(getCollectRefToursByIds(tourIds));
        cart.setTours(newTourList);
        cartRepository.save(cart);

    }

    @Override
    public boolean save(Cart cart) {
        cartRepository.save(cart);
        return true;
    }

    @Override
    public CartDto getCartByUser(String name) {
        logger.info("Create CartDto for User");
        User user = userService.findByName(name);
        if (user == null || user.getCart() == null) {
            return new CartDto();
        }

        CartDto cardDto = new CartDto();
        Map<Long, CartDetailDto> mapByTourId = new HashMap<>();

        List<Tour> tours = user.getCart().getTours();
        for (Tour tour : tours) {
            CartDetailDto detail = mapByTourId.get(tour.getId());
            if (detail == null) {
                mapByTourId.put(tour.getId(), new CartDetailDto(tour));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(tour.getPrice().toString()));
            }
        }
        logger.info("Calculate cost of tours");
        logger.info("Set CartDetailsDto in CartDto");
        cardDto.setCartDetails(new ArrayList<>(mapByTourId.values()));
        cardDto.aggregate();

        return cardDto;
    }

    @Override
    public CartDto deleteTourByUser(String name, Long tourId) {

        logger.info("Delete tour from User`s Cart");

        Cart cart = cartRepository.findByUserName(name);
        Tour tour = cart.getTours().stream().filter(t -> tourId.equals(t.getId())).findFirst().orElseThrow(NoSuchElementException::new);
        cart.getTours().remove(tour);
        Cart savedCart = cartRepository.save(cart);
        CartDto cardDto = new CartDto();
        User user = userService.findByName(name);
        Map<Long, CartDetailDto> mapByTourId = new HashMap<>();
        List<Tour> tours = user.getCart().getTours();
        for (Tour t : tours) {
            CartDetailDto detail = mapByTourId.get(tour.getId());
            if (detail == null) {
                mapByTourId.put(tour.getId(), new CartDetailDto(tour));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(tour.getPrice().toString()));
            }
        }

        cardDto.setCartDetails(new ArrayList<>(mapByTourId.values()));
        cardDto.aggregate();

        return cardDto;

    }

    @Override
    public void commitCartToOrder(String username) {
        logger.info("Create Order");
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Cart cart = user.getCart();
        if (cart == null || cart.getTours().isEmpty()) {
            return;
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Tour, Long> tourWithAmount = cart.getTours().stream()
                .collect(Collectors.groupingBy(tour -> tour, Collectors.counting()));

        List<OrderDetails> orderDetails = tourWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        logger.info("Set OrderDetails in Order, save Order in DB");
        order.setDetails(orderDetails);
        order.setSum(total);

        orderService.saveOrder(order);
        logger.info("Clear the User`s Cart");
        cart.getTours().clear();
        cartRepository.save(cart);
    }
}


