package com.project.travelAgency.service;

import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.model.entity.Tour;

import java.util.List;

public interface TourService {

    List<TourDto> getAll();

    void addToUserCart(Long tourId, String username);

    Tour getById(long id);

    boolean save(TourDto tourDto);

}
