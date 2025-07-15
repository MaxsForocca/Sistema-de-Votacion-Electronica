package com.sistema.document.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RemoteNodeService {

    private final List<String> otherNodeUrls;
    private final RestTemplate restTemplate;

    public RemoteNodeService(@Value("${document.other.nodes}") List<String> otherNodeUrls) {
        this.otherNodeUrls = otherNodeUrls;
        this.restTemplate = new RestTemplate();
    }

    public byte[] getFileFromOtherNodes(String filename) {
        for (String url : otherNodeUrls) {
            try {
                String fullUrl = url + "/documentos/" + filename;
                ResponseEntity<byte[]> response = restTemplate.getForEntity(fullUrl, byte[].class);
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    return response.getBody();
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}
