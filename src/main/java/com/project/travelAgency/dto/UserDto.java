package com.project.travelAgency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @Size(min=2, max=30,message = "Name must be between 2 and 30 characters")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$")
    private String matchingPassword;
    @Email(message = "Email should be valid")
    private String email;

}
