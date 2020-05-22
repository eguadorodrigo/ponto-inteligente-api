package br.com.chagas.pontointeligente.pontointeligente.api.dtos;

import java.util.Optional;

import lombok.Data;

@Data
public class LancamentoDto {

    private Optional<Long> id = Optional.empty();
    private String data;
    private String tipo;
    private String descricao;
    private String localizacao;
    private Long funcionarioId;
}