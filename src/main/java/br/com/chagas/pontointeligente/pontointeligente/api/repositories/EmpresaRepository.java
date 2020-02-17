package br.com.chagas.pontointeligente.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.chagas.pontointeligente.pontointeligente.api.model.Empresa;

@Transactional(readOnly=true)
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

    
    Empresa findByCnpj(String cnpj);
    
}