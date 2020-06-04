package br.com.chagas.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Usuario;
import br.com.chagas.pontointeligente.pontointeligente.api.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
    }

}