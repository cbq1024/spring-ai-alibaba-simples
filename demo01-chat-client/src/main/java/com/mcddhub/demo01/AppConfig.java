package com.mcddhub.demo01;

import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor(
            AdvisedRequest::userText,
            response -> "" + response.getResult()
        );
    }

}
