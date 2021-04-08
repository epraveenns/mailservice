package com.praveen.mailservice.payload;

import lombok.Data;

@Data
public class MailDto {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachmentUrl;
}
