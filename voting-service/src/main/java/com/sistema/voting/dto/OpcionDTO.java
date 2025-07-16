package com.sistema.voting.dto;

import lombok.Data;

@Data
public class OpcionDTO {
    private Long id;
    private String texto;
    private Long votacionId;
}
