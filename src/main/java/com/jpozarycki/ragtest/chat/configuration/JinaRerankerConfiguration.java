package com.jpozarycki.ragtest.chat.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "reranker.jina")
public class JinaRerankerConfiguration {
    private String url;
    private String apiKey;
    private String model;
}
