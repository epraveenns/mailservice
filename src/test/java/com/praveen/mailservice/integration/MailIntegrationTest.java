package com.praveen.mailservice.integration;

import com.praveen.mailservice.MailserviceApplication;
import com.praveen.mailservice.consumer.MailConsumer;
import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.payload.MailDto;
import com.praveen.mailservice.repository.MailLoggerRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(classes = MailserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka
public class MailIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MailLoggerRepository mailLoggerRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private MailConsumer mailConsumer;

    private static final String RECEIVER_TOPIC = "receivertopic";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, RECEIVER_TOPIC);

    private static final String MAIL_URI = "/api/v1/mail";

    @Test
    public void testSendingMailWithoutAttachment() throws InterruptedException {
        MailDto mailDto = new MailDto();
        mailDto.setSender("asf@example.com");
        mailDto.setRecipient("asdaf@example.com");
        mailDto.setBody("Body");
        mailDto.setSubject("Subject");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(MAIL_URI, mailDto, String.class);
        assertEquals(OK.value(), responseEntity.getStatusCode().value());
        mailConsumer.latch.await();
        assertEquals(0, mailConsumer.latch.getCount());

        Iterable<Mail> all = mailLoggerRepository.findAll();
        List<Mail> list = new ArrayList<>();
        all.forEach(list::add);

        assertEquals(1, list.size());
    }

    @Test
    public void testSendingMailWithAttachment() throws InterruptedException {
        MimeMessage mimeMessage = new MimeMessage((Session)null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MailDto mailDto = new MailDto();
        mailDto.setSender("asf@example.com");
        mailDto.setRecipient("asdaf@example.com");
        mailDto.setBody("Body");
        mailDto.setSubject("Subject");
        mailDto.setAttachmentUrl("https://www.cdc.gov/healthypets/images/pets/cute-dog-headshot.jpg");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(MAIL_URI, mailDto, String.class);
        assertEquals(OK.value(), responseEntity.getStatusCode().value());
        mailConsumer.latch.await();
        assertEquals(0, mailConsumer.latch.getCount());

        Iterable<Mail> all = mailLoggerRepository.findAll();
        List<Mail> list = new ArrayList<>();
        all.forEach(list::add);

        assertEquals(1, list.size());

        assertTrue(Files.exists(Path.of("filestore/cute-dog-headshot.jpg")));
    }
}
