package com.sistema.document.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "document")
public class NodeProperties {

    private String nodeIp;
    private List<String> otherNodes;
    private String storagePath;
    private boolean isCoordinator;

}
