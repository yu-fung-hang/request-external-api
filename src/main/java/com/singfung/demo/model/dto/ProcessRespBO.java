package com.singfung.demo.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProcessRespBO {
    private String rsp_code;
    private String rsp_msg;
    private String data;
    private String md5;

    public ProcessRespBO(String rsp_code, String rsp_msg) {
        this.rsp_code = rsp_code;
        this.rsp_msg = rsp_msg;
    }
}