package com.praveen.mailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppEnv {

    private final Environment env;

    @Autowired
    public AppEnv(Environment env) {
        this.env = env;
    }

    public String getEmailTopicName() {
        return env.getRequiredProperty("kafka.email.topic.name", String.class);
    }

    public String getEmailGroupId() {
        return env.getRequiredProperty("kafka.email.group.id", String.class);
    }

    public String getKafkaBootstrapAddress() {
        return env.getRequiredProperty("kafka.bootstrap-servers", String.class);
    }

    public String getLocalFileStoragePath() {
        return env.getRequiredProperty("filestorage.local.path", String.class);
    }
}
