package br.com.chagas.pontointeligente.pontointeligente.api.security.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String token;

    public TokenDto(String token) {
        this.token = token;
    }

}