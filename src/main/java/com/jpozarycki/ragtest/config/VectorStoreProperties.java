package com.jpozarycki.ragtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.properties")
public class VectorStoreProperties {
    private List<Resource> documentsToLoad;
}
