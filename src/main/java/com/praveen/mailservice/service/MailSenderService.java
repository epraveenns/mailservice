package com.praveen.mailservice.service;

import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.repository.MailLoggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Slf4j
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final MailLoggerRepository mailLoggerRepository;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender, MailLoggerRepository mailLoggerRepository) {
        this.javaMailSender = javaMailSender;
        this.mailLoggerRepository = mailLoggerRepository;
    }

    @Retryable(value = Exception.class,
            maxAttemptsExpression = "${mailer.retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${mailer.retry.maxDelay}"
    ))
    public void sendEmail(Mail mail, File attachment) throws Exception {
        log.info("Sending email");
        if(attachment != null) {
            sendEmailWithAttachment(mail, attachment);
        } else {
            sendSimpleEmail(mail);
        }
        log.info("Email sent");
        mailLoggerRepository.save(mail);
    }

    private void sendSimpleEmail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getRecipient());
        message.setFrom(mail.getSender());
        message.setSubject(mail.getSubject());
        message.setText(mail.getBody());
//      javaMailSender.send(message);         //can be uncommented if we have smtp server configured
    }

    private void sendEmailWithAttachment(Mail mail, File file) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.addAttachment(file.getName(), file);
            helper.setTo(mail.getRecipient());
            helper.setFrom(mail.getSender());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody());
//            javaMailSender.send(message);         //can be uncommented if we have smtp server configured
        } catch (Exception e) {
            log.error("Exception occurred. This operation might be retried", e);
            throw e;
        }
    }

}
