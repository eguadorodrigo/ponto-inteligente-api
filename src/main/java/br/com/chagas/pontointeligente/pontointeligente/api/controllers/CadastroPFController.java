package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import br.com.chagas.pontointeligente.pontointeligente.api.dtos.CadastroPFDto;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.PerfilEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.response.Response;
import br.com.chagas.pontointeligente.pontointeligente.api.services.EmpresaService;
import br.com.chagas.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.chagas.pontointeligente.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

    public static final Logger logger = LoggerFactory.getLogger(CadastroPFController.class);

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    public CadastroPFController() {

    }

    /**
     * Cadastra um funcionario pessoa física no sistema
     * 
     * @param cadastroPFDto
     * @param result
     * @return ResponseEntity < Response < CadastroPFDto > >
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
            BindingResult result) throws NoSuchAlgorithmException {
        logger.info("Cadastrando PF: {}", cadastroPFDto.toString());
        Response<CadastroPFDto> response = new Response<CadastroPFDto>();

        validarDadosExistentes(cadastroPFDto, result);

        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);

        if (result.hasErrors()) {
            logger.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica se a empresa ou funcionario já existem na base de dados.
     * 
     * @param cadastroPJDto
     * @param result
     */
    private void validarDadosExistentes(@Valid CadastroPFDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()) {
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
        }
        this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
        this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe")));
    }

    /**
     * Converte os dados do DTO para Funcionario
     * 
     * @param cadastroPJDto
     * @param result
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(@Valid CadastroPFDto cadastroPFDto, BindingResult result)
            throws NoSuchAlgorithmException {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

        cadastroPFDto.getQtdHorasTrabalhoDia()
                .ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));

        cadastroPFDto.getValorHora()
                .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        return funcionario;
    }

    /**
     * Converte o CadastroPFDto para Funcionario.
     * 
     * @param funcionario
     * @return CadastroPFDto
     */
    private CadastroPFDto converterCadastroPFDto(Funcionario funcionario){
        CadastroPFDto cadastroPFDto = new CadastroPFDto();
        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setNome(funcionario.getNome());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        
        funcionario.getValorHoraOpt()
            .ifPresent(valorHoraOpt -> cadastroPFDto.setValorHora(Optional.of(valorHoraOpt.toString())));
        funcionario.getQtdHorasTrabalhadoDiasOpt()
            .ifPresent(qtdHorasTrabalhadoDiasOpt -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhadoDiasOpt))));
        funcionario.getQtdHorasAlmocoOpt()
            .ifPresent(qtdHorasAlmocoOpt -> cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmocoOpt))));
            
        return cadastroPFDto;
    }
}