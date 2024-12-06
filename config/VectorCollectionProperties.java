package com.giyeon.cbnuchatbot.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.vectorstore.milvus")
@Getter
@Setter
public class VectorCollectionProperties {
    private String databaseName;
    private String collectionName;
}