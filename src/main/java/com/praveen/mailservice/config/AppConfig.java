package com.praveen.mailservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.mailservice.AppEnv;
import com.praveen.mailservice.model.Mail;
import com.praveen.mailservice.payload.MailDto;
import com.praveen.mailservice.serde.MailDeserializer;
import com.praveen.mailservice.serde.MailSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRetry
public class AppConfig {

    private final AppEnv env;

    @Autowired
    public AppConfig(AppEnv env) {
        this.env = env;
    }

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        docket.useDefaultResponseMessages(false);
        return docket;
    }

    @Bean
    public ProducerFactory<String, Mail> mailProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getKafkaBootstrapAddress());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MailSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Mail> mailKafkaTemplate() {
        return new KafkaTemplate<String, Mail>(mailProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Mail> mailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Mail> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(mailConsumerFactory(env.getEmailGroupId()));
        return factory;
    }

    public ConsumerFactory<String, Mail> mailConsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getKafkaBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new MailDeserializer());
    }
}
