package com.praveen.mailservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.service.MailProducerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MailController.class)
class MailControllerTest {
    @MockBean
    private MailProducerService mailProducerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void sendMail() throws Exception {
        doNothing().when(mailProducerService).sendEmail(Mockito.any(Mail.class), Mockito.anyString());
        mockMvc.perform(
                post("/api/v1/mail")
                .content(new ObjectMapper().writeValueAsString(new Mail()))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is(202));
    }
}