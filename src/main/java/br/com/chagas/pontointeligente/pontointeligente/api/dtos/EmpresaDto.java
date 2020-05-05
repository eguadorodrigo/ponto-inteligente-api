package br.com.chagas.pontointeligente.pontointeligente.api.dtos;

import lombok.Data;

@Data
public class EmpresaDto {

    private Long id;

    private String razaoSocial;

    private String cnpj;
}
