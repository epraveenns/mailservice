package com.praveen.mailservice.consumer;

import com.praveen.mailservice.filestorage.FileStorageInf;
import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component

public class MailConsumer {
    private final MailSenderService mailSenderService;
    private final FileStorageInf fileStorageInf;
    public CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    public MailConsumer(MailSenderService mailSenderService, FileStorageInf fileStorageInf) {
        this.mailSenderService = mailSenderService;
        this.fileStorageInf = fileStorageInf;
    }

    @KafkaListener(topics = "${kafka.email.topic.name}", groupId = "${kafka.email.group.id}", containerFactory = "mailKafkaListenerContainerFactory")
    @Async
    public void mailConsumer(Mail mail) throws Exception {
        log.info("Received mail in consumer");
        File file = null;
        if(Strings.isNotEmpty(mail.getAttachmentUrl())) {
            file = fileStorageInf.getFile(mail.getAttachmentUrl());
        }
        mailSenderService.sendEmail(mail, file);
        latch.countDown();
    }

}