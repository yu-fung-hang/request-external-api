package com.singfung.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author sing-fung
 * @since 2/20/2023
 */

@Data
public class UserDTO {
    Integer id;

    String username;
    String password;
    String email;

    UserStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date ts;
}

enum UserStatus {
    enabled, forbidden
}