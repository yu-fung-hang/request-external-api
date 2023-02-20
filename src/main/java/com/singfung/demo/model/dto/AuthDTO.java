package com.singfung.demo.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author sing-fung
 * @since 2/20/2023
 */

@Data
public class AuthDTO {
    @NotBlank(message = "username/email cannot be empty")
    private String usernameOrEmail;
    @NotBlank(message = "password cannot be empty")
    private String password;
}
