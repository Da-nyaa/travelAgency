package com.project.travelAgency.dto;

import com.project.travelAgency.model.entity.Country;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.entity.TypeOfTour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDto {

    private Long id;
    private TypeOfTour typeOfTour;
    private Country country;
    private Integer days;
    private BigDecimal price;
    private BigDecimal amount;
    private Double sum;

    public CartDetailDto(Tour tour) {
        this.id = tour.getId();
        this.typeOfTour = tour.getTypeOfTour();
        this.country = tour.getCountry();
        this.days = tour.getDays();
        this.price = tour.getPrice();
        this.amount = new BigDecimal(1.0);
        this.sum = Double.valueOf(tour.getPrice().toString());
    }
}
