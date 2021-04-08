package com.praveen.mailservice.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailDto {
    @NotNull
    private String sender;

    @NotNull
    private String recipient;
    private String subject;
    private String body;
    private String attachmentUrl;
}
