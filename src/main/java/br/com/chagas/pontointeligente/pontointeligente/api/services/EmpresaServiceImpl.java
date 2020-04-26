package br.com.chagas.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.repositories.EmpresaRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    public static final Logger logger =  LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;


    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        logger.info("Buscando uma empresa por CNPJ: {}", cnpj);
        return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        logger.info("Persistindo empresa: {}", empresa);
        return this.empresaRepository.save(empresa);
    }
    
}