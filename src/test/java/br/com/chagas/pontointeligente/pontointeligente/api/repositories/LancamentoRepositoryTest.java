package br.com.chagas.pontointeligente.pontointeligente.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Lancamento;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.PerfilEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.TipoEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Long  funcionarioId;

    @Before
    public void setUp(){
        final Empresa empresa = empresaRepository.save(obterDadosEmpresa());
        
        Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
        this.funcionarioId = funcionario.getId();
      this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
    }
    
    @After
    public final void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioId(){
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);

        assertEquals(2, lancamentos.size());
    }


    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado(){
        PageRequest page = PageRequest.of(0, 10);

        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);

        assertEquals(2, lancamentos.getTotalElements());
    }

    
    private Lancamento obterDadosLancamentos(Funcionario funcionario){
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }

    private Funcionario obterDadosFuncionario(final Empresa empresa) {
        final Funcionario funcionario = new Funcionario();
        funcionario.setNome("Fulano de tal");
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("1234567"));
        funcionario.setCpf("12345678987");
        funcionario.setEmail("email@passado.com");
        funcionario.setEmpresa(empresa);
        return funcionario;
    }
    
    private Empresa obterDadosEmpresa() {
        final Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa Exemplo");
        empresa.setCnpj("123456000116");
        return empresa;
    }
    
}