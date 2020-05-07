package br.com.chagas.pontointeligente.pontointeligente.api.dtos;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class FuncionarioDto {

    private Long id;
    
    @NotEmpty(message = "Nome não pode ser vazio.")
    @Length(min=3, max= 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    private String nome; 

    @NotEmpty(message = "Email não pode ser vazio.")
    @Length(min=5, max= 200, message = "Email deve conter entre 5 e 200 caracteres.")
    private String email;

    private Optional<String> senha = Optional.empty();

    private Optional<String> valorHora = Optional.empty();

    private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
    
    private Optional<String> qtdHorasAlmoco  = Optional.empty();    
    
}