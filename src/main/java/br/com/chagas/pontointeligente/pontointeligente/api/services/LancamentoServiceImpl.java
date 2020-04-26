package br.com.chagas.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Lancamento;
import br.com.chagas.pontointeligente.pontointeligente.api.repositories.LancamentoRepository;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    public static final Logger logger = LoggerFactory.getLogger(LancamentoServiceImpl.class);

    @Autowired
    LancamentoRepository lancamentoRepository;
    
    @Override
    public Page<Lancamento> buscarFuncionarioId(final Long funcionarioId, final PageRequest pageRequest) {
        logger.info("Buscando lancamentos para o funcionario ID: {}", funcionarioId);
        return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    @Override
    public Optional<Lancamento> buscarPorId(final Long id) {
        logger.info("Buscando lancamento para o ID: {}", id);
        return this.lancamentoRepository.findById(id);
    }

    @Override
    public Lancamento persistir(final Lancamento lancamento) {
        logger.info("Persistindo o lancamento: {}", lancamento);
        return this.lancamentoRepository.save(lancamento);
    }

    @Override
    public void remover(final Long id) {
        logger.info("Removendo o lancamento para o ID: {}", id);
        this.lancamentoRepository.deleteById(id);
    }
    
}