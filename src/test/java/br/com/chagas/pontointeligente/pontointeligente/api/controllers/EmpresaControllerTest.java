package br.com.chagas.pontointeligente.pontointeligente.api.controllers;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.chagas.pontointeligente.pontointeligente.api.entities.Empresa;
import br.com.chagas.pontointeligente.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpresaControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmpresaService empresaService;

    private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
    private static final Long ID = Long.valueOf(1);
    private static final String CNPJ = "123465000187";
    private static final String RAZAO_SOCIAL = "Empresa XYZ";

    @Test
    public void testBuscarEmpresaCnpjInvalido() throws Exception{
        BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                .get(BUSCAR_EMPRESA_CNPJ_URL)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.content().contentType("application/    json;charset=UTF-8"))
             .andExpect(MockMvcResultMatchers.jsonPath("$.erros").value("Empresa não encontrada para o CNPJ" + CNPJ));
    }

    
    @Test
    public void testBuscarEmpresaCnpjValido(){
        BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString()))
                .willReturn(Optional.of(this.obterDadosEmpresa()));

        mvc.perform(((ResultActions) MockMvcRequestBuilders
                .get(BUSCAR_EMPRESA_CNPJ_URL)
                .accept(MediaType.APPLICATION_JSON  
            )).andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(ID))
             .andExpect(MockMvcResultMatchers.jsonPath("$.data.razaoSocial").value(RAZAO_SOCIAL)));
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(ID);
        empresa.setRazaoSocial(RAZAO_SOCIAL);
        empresa.setCnpj(CNPJ);
        return empresa;
    }


   
}