package br.com.chagas.pontointeligente.pontointeligente.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.PerfilEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "email@email.com";
    private static final String CPF = "12345678989";

    @Before
    public void setUp(){
        final Empresa empresa = empresaRepository.save(obterDadosEmpresa());
        this.funcionarioRepository.save(obterDadosFuncionario(empresa));
    }

    @After
    public final void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        final Funcionario funcionario = funcionarioRepository.findByEmail(EMAIL);
        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCpf() {
        final Funcionario funcionario = funcionarioRepository.findByCpf(CPF);
        assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpf() {
        final Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
        final Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");
        assertNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpfParaCpfInvalido() {
        final Funcionario funcionario = funcionarioRepository.findByCpfOrEmail("32165498787", EMAIL);
        assertNull(funcionario);
    }

    private Funcionario obterDadosFuncionario(final Empresa empresa) {
        final Funcionario funcionario = new Funcionario();
        funcionario.setNome("Fulano de tal");
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("1234567"));
        funcionario.setCpf(CPF);
        funcionario.setEmail(EMAIL);
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