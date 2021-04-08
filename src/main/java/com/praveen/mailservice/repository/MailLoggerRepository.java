package com.praveen.mailservice.repository;

import com.praveen.mailservice.model.Mail;
import org.springframework.data.repository.CrudRepository;

public interface MailLoggerRepository extends CrudRepository<Mail, Long> {
}
