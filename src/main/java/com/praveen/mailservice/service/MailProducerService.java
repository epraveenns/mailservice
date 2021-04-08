package com.praveen.mailservice.service;

import com.praveen.mailservice.AppEnv;
import com.praveen.mailservice.filestorage.FileStorageInf;
import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.util.FileDownloadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Service
@Slf4j
public class MailProducerService {

    private final KafkaTemplate<String, Mail> mailKafkaTemplate;
    private final AppEnv env;
    private final FileStorageInf fileStorageService;

    @Autowired
    public MailProducerService(KafkaTemplate<String, Mail> mailKafkaTemplate, AppEnv env,
                               FileStorageInf fileStorageService) {
        this.mailKafkaTemplate = mailKafkaTemplate;
        this.env = env;
        this.fileStorageService = fileStorageService;
    }

    public void sendEmail(Mail mail, String attachmentUrl) throws IOException {
        if(Strings.isNotEmpty(attachmentUrl)) {
            File attachment = FileDownloadUtil.downloadFile(attachmentUrl);

            //saving the file in our storage space to avoid failures if file was removed from user's link later while sending email
            String storedFilePath = fileStorageService.storeFile(attachment);
            mail.setAttachmentUrl(storedFilePath);
        }

        ListenableFuture<SendResult<String, Mail>> future = mailKafkaTemplate.send(env.getEmailTopicName(), new Random().nextLong()+"", mail);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Mail>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Unable to queue email. Exception is {}", throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Mail> sendResult) {
                log.info("Successfully queued email {}:{}", sendResult.getProducerRecord().key(), sendResult.getProducerRecord().value());
            }
        });
    }
}
