package com.sistema.votacion.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
// Este archivo configura el ModelMapper como un bean de Spring para que pueda ser inyectado en otros componentes de la aplicación.
// ModelMapper es una biblioteca que facilita la conversión entre objetos de diferentes tipos, como DTOs y entidades JPA.