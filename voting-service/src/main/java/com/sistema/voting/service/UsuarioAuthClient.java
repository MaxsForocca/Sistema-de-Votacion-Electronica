package com.sistema.voting.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.sistema.voting.dto.UsuarioVoting;
@Service
public class UsuarioAuthClient {
    private final RestTemplate restTemplate;
    private final String authServiceUrl = "http://localhost:8082"; // URL base de auth-service

    public UsuarioAuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<UsuarioVoting> obtenerUsuario(Long id) {
        try {
            ResponseEntity<UsuarioVoting> response = restTemplate.getForEntity(
                authServiceUrl + "/usuario/" + id, UsuarioVoting.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con auth-service: " + e.getMessage());
        }
    }
}
