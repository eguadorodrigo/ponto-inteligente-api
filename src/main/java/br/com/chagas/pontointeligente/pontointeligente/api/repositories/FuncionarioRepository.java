package br.com.chagas.pontointeligente.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.chagas.pontointeligente.pontointeligente.api.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
    
}