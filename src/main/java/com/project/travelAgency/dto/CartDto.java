package com.project.travelAgency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private Long id;
    private int amountTours;
    private Double sum;
    private List<CartDetailDto> cartDetails = new ArrayList<>();

    public void aggregate() {
        this.amountTours = cartDetails.size();
        this.sum = cartDetails.stream()
                .map(CartDetailDto::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}
