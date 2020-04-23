package br.com.chagas.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.repositories.FuncionarioRepository;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    public static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    @Override
    public Funcionario persistir(Funcionario funcionario) {
        logger.info("Persistindo funcionario: {}", funcionario);
        return this.funcionarioRepository.save(funcionario);
    }

    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        logger.info("Buscando funcionario pelo CPF: {}", cpf);
        return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        logger.info("Buscando funcionario pelo EMAIL: {}", email);
        return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        logger.info("Buscando funcionario pelo ID: {}", id);
        Example example = Example.create(Funcionario.class);

        return Optional.ofNullable(this.funcionarioRepository.findOne(id));
    }
   
}