package com.praveen.mailservice;

import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.payload.MailDto;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
public class MailserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailserviceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(MailDto.class, Mail.class).addMappings(mapper -> {
			mapper.map(src -> null, Mail::setAttachmentUrl);
		});

		return modelMapper;
	}
}
