package com.sistema.document.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Service
public class RemoteNodeService {

    private final List<String> otherNodeUrls;
    private final RestTemplate restTemplate;

    public RemoteNodeService(@Value("${document.other.nodes}") String nodesRaw) {
        this.otherNodeUrls = List.of(nodesRaw.split(","));
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    public void init() {
        System.out.println("Nodos configurados: " + otherNodeUrls);
    }

    public byte[] getFileFromOtherNodes(String filename, List<String> visitedNodes) {
        for (String url : otherNodeUrls) {
            if (visitedNodes.contains(url)) continue; // evitar ciclo

            try {
                String fullUrl = "http://" + url + ":8084/documentos/" + filename;
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Visited-Nodes", String.join(",", visitedNodes));
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<byte[]> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
                );

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    return response.getBody();
                }
            } catch (Exception e) {
                System.out.println("Error al consultar " + url + ": " + e.getMessage());
            }
        }
        return null;
    }

}
