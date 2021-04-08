package com.praveen.mailservice.controller;

import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.payload.MailDto;
import com.praveen.mailservice.service.MailProducerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mail")
@Slf4j
public class MailController {
    private final MailProducerService mailProducerService;
    private final ModelMapper modelMapper;

    @Autowired
    public MailController(MailProducerService mailProducerService, ModelMapper modelMapper) {
        this.mailProducerService = mailProducerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Send email", response = String.class, notes =
            "API to asynchronously send an email")
    @ApiResponses(
            value = {
                    @ApiResponse(message = "Accepted", code = 202),
            }
    )
    public String sendEmail(@RequestBody @Valid MailDto mailDto) throws IOException {
        log.info("Received a request for delivering email {}", mailDto);
        mailProducerService.sendEmail(modelMapper.map(mailDto, Mail.class), mailDto.getAttachmentUrl());
        return "Request Accepted. Your email has been queued for delivery";
    }
}
