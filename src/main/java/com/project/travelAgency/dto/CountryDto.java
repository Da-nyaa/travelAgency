package com.project.travelAgency.dto;

import com.project.travelAgency.model.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDto {

    @NotBlank(message = "Field Country should not be empty ")
    private String country;

    @NotBlank(message = "Field City should not be empty ")
    private String city;

    public CountryDto(Country country) {
        this.country = country.getCountry();
        this.city = country.getCity();
    }
}
