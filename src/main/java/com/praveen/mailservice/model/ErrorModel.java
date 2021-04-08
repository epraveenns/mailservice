package com.praveen.mailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorModel {
    String status;
    String error;
}
