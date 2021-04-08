package com.praveen.mailservice.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.mailservice.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MailDeserializer implements Deserializer<Mail> {

    @Override
    public Mail deserialize(String s, byte[] bytes) {
        Mail mail = new Mail();
        try {
            mail = new ObjectMapper().readValue(bytes, Mail.class);
        }
        catch (IOException e) {
            log.error("Error during mail object deserialization", e);
        }
        return mail;
    }
}
