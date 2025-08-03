package net.fiv.GitHubRepositoryTestTask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfiguration {
    @Bean
    public RestClient restClient(RestClient.Builder builder, @Value("${rest-client.base-url}") final String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}