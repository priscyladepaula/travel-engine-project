package br.com.priscyladepaula.travelengine.controller;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.factory.ProvedorVooFactory;
import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import br.com.priscyladepaula.travelengine.template.ExecucaoBuscaVooService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VooController.class)
class VooControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProvedorVooFactory provedorVooFactory;

    @MockitoBean
    private ExecucaoBuscaVooService execucaoBuscaVooService;

    @Test
    void deveRetornarListaDeVoosQuandoRequisicaoForValida() throws Exception {
        LocalDate data = LocalDate.of(2026, 12, 10);
        ProvedorVooService provedorMock = mock(ProvedorVooService.class);

        VooDTO voo = new VooDTO(
                "ViajeMais Air",
                "VM-991",
                "GRU",
                "REC",
                data.atTime(8, 0),
                data.atTime(11, 30),
                new BigDecimal("450.00")
        );

        when(provedorVooFactory.obterProvedor("VIAJEMAIS")).thenReturn(provedorMock);
        when(provedorMock.buscarVoos("GRU", "REC", data)).thenReturn(List.of(voo));

        mockMvc.perform(get("/api/v1/voos/buscar")
                        .param("parceiro", "VIAJEMAIS")
                        .param("origem", "GRU")
                        .param("destino", "REC")
                        .param("data", "2026-12-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companhia").value("ViajeMais Air"))
                .andExpect(jsonPath("$[0].codigoVoo").value("VM-991"))
                .andExpect(jsonPath("$[0].origem").value("GRU"))
                .andExpect(jsonPath("$[0].destino").value("REC"))
                .andExpect(jsonPath("$[0].preco").value(450.00));
    }

    @Test
    void deveRetornarBadRequestQuandoParceiroNaoExistir() throws Exception {
        when(provedorVooFactory.obterProvedor("DESCONHECIDO"))
                .thenThrow(new IllegalArgumentException("Provedor de voo não encontrado para o código: DESCONHECIDO"));

        mockMvc.perform(get("/api/v1/voos/buscar")
                        .param("parceiro", "DESCONHECIDO")
                        .param("origem", "GRU")
                        .param("destino", "REC")
                        .param("data", "2026-12-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Provedor de voo não encontrado para o código: DESCONHECIDO"));
    }

    @Test
    void deveRetornarVoosAgregadosQuandoBuscarTodosForValido() throws Exception {
        LocalDate data = LocalDate.of(2026, 12, 10);

        VooDTO voo1 = new VooDTO(
                "VoaBrasil Linhas Aéreas",
                "VB-305",
                "GRU",
                "REC",
                data.atTime(6, 45),
                data.atTime(9, 55),
                new BigDecimal("389.90")
        );

        VooDTO voo2 = new VooDTO(
                "ViajeMais Air",
                "VM-991",
                "GRU",
                "REC",
                data.atTime(8, 0),
                data.atTime(11, 30),
                new BigDecimal("450.00")
        );

        when(execucaoBuscaVooService.buscarEmTodosParceiros("GRU", "REC", data))
                .thenReturn(List.of(voo1, voo2));

        mockMvc.perform(get("/api/v1/voos/buscar-todos")
                        .param("origem", "GRU")
                        .param("destino", "REC")
                        .param("data", "2026-12-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].codigoVoo").value("VB-305"))
                .andExpect(jsonPath("$[0].preco").value(389.90))
                .andExpect(jsonPath("$[1].codigoVoo").value("VM-991"))
                .andExpect(jsonPath("$[1].preco").value(450.00));
    }

    @Test
    void deveRetornarBadRequestNoBuscarTodosQuandoParametrosForemInvalidos() throws Exception {
        LocalDate data = LocalDate.of(2026, 12, 10);

        when(execucaoBuscaVooService.buscarEmTodosParceiros("GRU", "GRU", data))
                .thenThrow(new IllegalArgumentException("Origem e destino não podem ser iguais."));

        mockMvc.perform(get("/api/v1/voos/buscar-todos")
                        .param("origem", "GRU")
                        .param("destino", "GRU")
                        .param("data", "2026-12-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Origem e destino não podem ser iguais."));
    }
}
