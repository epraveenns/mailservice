package com.praveen.mailservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Mail {
    @Id
    @GeneratedValue
    private Long id;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String attachmentUrl;
}
