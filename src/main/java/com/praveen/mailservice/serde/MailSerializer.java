package com.praveen.mailservice.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.mailservice.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSerializer implements Serializer<Mail> {

    @Override
    public byte[] serialize(String s, Mail mail) {
        byte[] bytes = new byte[0];
        try {
            bytes = new ObjectMapper().writeValueAsBytes(mail);
        }
        catch (JsonProcessingException e) {
            log.error("Error during serialization of mail data", e);
        }
        return bytes;
    }
}
