package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.chagas.pontointeligente.pontointeligente.api.dtos.CadastroPJDto;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.PerfilEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.response.Response;
import br.com.chagas.pontointeligente.pontointeligente.api.services.EmpresaService;
import br.com.chagas.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.chagas.pontointeligente.pontointeligente.api.utils.PasswordUtils;


@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {
    
    public static final Logger logger = LoggerFactory.getLogger(CadastroPJController.class);


    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    
    public CadastroPJController(){

    }
    
    /**
     * 
     * @param cadastroPJDto
     * @param result 
     * @return ResponseEntity<Response<CadastroPJDto>>
     */
    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto, 
        BindingResult result) throws NoSuchAlgorithmException {
        logger.info("Cadastrando PJ: {}", cadastroPJDto.toString());
        Response<CadastroPJDto> response = new Response<CadastroPJDto>();

        validarDadosExistentes(cadastroPJDto, result);

        Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto,result);

        if(result.hasErrors()){
            logger.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPJDto(funcionario));
        return ResponseEntity.ok(response);
    }


    /**
     * Verifica se a empresa ou funcionario já existem na base de dados.
     * @param cadastroPJDto
     * @param result
     */
    private void validarDadosExistentes(@Valid CadastroPJDto cadastroPJDto, BindingResult result) {
        this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
            .ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existe.")));
        this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
        this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe")));
    }

    /**
     * Converte os dados do DTO para Empresa.
     * @param cadastroPJDto
     * @return Empresa
     */
    private Empresa converterDtoParaEmpresa(@Valid CadastroPJDto cadastroPJDto) {
       Empresa empresa = new Empresa();
       empresa.setCnpj(cadastroPJDto.getCnpj());
       empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

        return empresa;
    }

    /**
     * Converte os dados do DTO para Funcionario
     * @param cadastroPJDto
     * @param result
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(@Valid CadastroPJDto cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException{
        
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPJDto.getNome());
        funcionario.setEmail(cadastroPJDto.getEmail());
        funcionario.setCpf(cadastroPJDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));

        return funcionario;
    }

    

    private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
        CadastroPJDto cadastroPJDto = new CadastroPJDto();
        cadastroPJDto.setId(funcionario.getId());
        cadastroPJDto.setNome(funcionario.getNome());
        cadastroPJDto.setEmail(funcionario.getEmail());
        cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
        cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
        cadastroPJDto.setCpf(funcionario.getCpf());
        return cadastroPJDto;
    }

    
    

}