package br.com.chagas.pontointeligente.pontointeligente.api.services;

import java.util.Optional;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Usuario;

public interface UsuarioService {

    /**
     * Busca e retorna um usuário dado um email.
     *
     * @param email
     * @return Optional<Usuario>
     */
    Optional<Usuario> buscarPorEmail(String email);
}