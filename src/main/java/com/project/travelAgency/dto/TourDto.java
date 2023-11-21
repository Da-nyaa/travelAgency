package com.project.travelAgency.dto;

import com.project.travelAgency.model.entity.Status;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.entity.TypeOfTour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDto {

    private Long id;
    @NotNull
    private TypeOfTour typeOfTour;
    @Valid
    private CountryDto countryDto;
    @Min(value = 3, message = "Number of days must be greater than 3")
    @Max(value = 15, message = "Number of days must be smaller than 15")
    private int days;
    @DecimalMin(value = "100.00", inclusive = true, message = "Price should not be smaller then 100.00")
    private BigDecimal price;
    @NotNull(message = "Field Status should not be empty ")
    private Status status;


    public TourDto(Tour tour) {
        this.id = tour.getId();
        this.typeOfTour = tour.getTypeOfTour();
        this.countryDto = new CountryDto(tour.getCountry());
        this.days = tour.getDays();
        this.price = tour.getPrice();
        this.status = tour.getStatus();
    }
}
