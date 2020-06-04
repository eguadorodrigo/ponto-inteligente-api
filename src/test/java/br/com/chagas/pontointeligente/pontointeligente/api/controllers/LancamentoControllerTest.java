package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.chagas.pontointeligente.pontointeligente.api.dtos.LancamentoDto;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Funcionario;
import br.com.chagas.pontointeligente.pontointeligente.api.entities.Lancamento;
import br.com.chagas.pontointeligente.pontointeligente.api.enuns.TipoEnum;
import br.com.chagas.pontointeligente.pontointeligente.api.services.FuncionarioService;
import br.com.chagas.pontointeligente.pontointeligente.api.services.LancamentoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LancamentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LancamentoService lancamentoService;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String URL_BASE = "/api/lancamentos/";
    private static final Long ID_FUNCIONARIO = 5L;
    private static final Long ID_LANCAMENTO = 7L;
    private static final String TIPO = TipoEnum.INICIO_TRABALHO.name();
    private static final Date DATA = new Date();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @WithMockUser
    public void testCadastrarLancamento() throws Exception {
        Lancamento lancamento = obterDadoslancamento();
        BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong()))
                .willReturn(Optional.of(new Funcionario()));
        BDDMockito.given(this.lancamentoService.persistir(Mockito.any(Lancamento.class))).willReturn(lancamento);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(ID_LANCAMENTO))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tipo").value(TIPO))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data").value(this.dateFormat.format(DATA)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.funcionarioId").value(ID_FUNCIONARIO))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros").isEmpty());

    }


    @Test
    @WithMockUser
    public void testCadastrarLancamentoFuncionarioIdInvalido() throws Exception {
        BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
            .content(this.obterJsonRequisicaoPost())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.erros").value("Funcionario não encontrado. ID inexistente."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
                

    }

    @Test
    @WithMockUser(username= "admin@admin.com", roles={"ADMIN"})
    public void testRemoverLancamento() throws Exception {
        BDDMockito.given(this.lancamentoService.buscarPorId(Mockito.anyLong()))
                .willReturn(Optional.of(new Lancamento()));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser
    public void testRemoverLancamentoAcessoNegado() throws Exception {
        BDDMockito.given(this.lancamentoService.buscarPorId(Mockito.anyLong()))
                .willReturn(Optional.of(new Lancamento()));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(null);
        lancamentoDto.setData(this.dateFormat.format(DATA));
        lancamentoDto.setTipo(TIPO);
        lancamentoDto.setFuncionarioId(ID_FUNCIONARIO);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(lancamentoDto));
        return mapper.writeValueAsString(lancamentoDto);
    }

    
    private Lancamento obterDadoslancamento() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(ID_LANCAMENTO);
        lancamento.setData(DATA);
        lancamento.setTipo(TipoEnum.valueOf(TIPO));
        lancamento.setFuncionario(new Funcionario());
        lancamento.getFuncionario().setId(ID_FUNCIONARIO);
        return lancamento;
    }
}