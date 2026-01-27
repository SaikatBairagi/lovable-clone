package com.apiorbit.lovableclone.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }
}
