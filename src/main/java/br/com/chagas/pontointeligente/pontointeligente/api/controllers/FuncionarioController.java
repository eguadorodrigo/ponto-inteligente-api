package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.chagas.pontointeligente.pontointeligente.api.dtos.FuncionarioDto;
import br.com.chagas.pontointeligente.pontointeligente.api.response.Response;
import br.com.chagas.pontointeligente.pontointeligente.api.services.FuncionarioService;


@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
    
    public static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);


    @Autowired
    private FuncionarioService funcionarioService;

    public FuncionarioController(){}

    @PutMapping(value="path/{id}")
    public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioDto funcionarioDto, @BindingResult result) throws NoSuchAlgorithmException {
        Response<FuncionarioDto> response = new Response<FuncionarioDto>();
        
        return null;
    }

}