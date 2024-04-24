package com.singfung.demo.model.dto;

import lombok.Data;

@Data
public class SecureAccessRequest {
    private String partner_code;
    private String action;
    private String version;
    private String data;
    private String md5;
    private String merchant_id;
}