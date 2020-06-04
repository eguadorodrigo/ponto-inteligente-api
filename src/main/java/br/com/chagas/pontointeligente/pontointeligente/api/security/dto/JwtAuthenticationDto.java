package br.com.chagas.pontointeligente.pontointeligente.api.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class JwtAuthenticationDto {

    @NotEmpty(message = "Email não pode ser vazio.")
    @Email(message = "Email inválido.")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazia.")
    private String senha;

    public JwtAuthenticationDto(){
        
    }

}