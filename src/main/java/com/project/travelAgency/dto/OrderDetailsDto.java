package com.project.travelAgency.dto;

import com.project.travelAgency.model.entity.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {
    private String tour;
    private Double price;
    private Double amount;
    private Double sum;

    public OrderDetailsDto(OrderDetails details) {
        this.tour = details.getTour().getTypeOfTour().name();
        this.price = details.getPrice().doubleValue();
        this.amount = details.getAmount().doubleValue();
        this.sum = details.getPrice().multiply(details.getAmount()).doubleValue();
    }
}

