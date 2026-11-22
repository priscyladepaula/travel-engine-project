package br.com.priscyladepaula.travelengine.service;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.factory.ProvedorVooFactory;
import br.com.priscyladepaula.travelengine.template.ExecucaoBuscaVooService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExecucaoBuscaVooServiceTest {

    @Mock
    private ProvedorVooFactory provedorVooFactory;

    @Mock
    private ProvedorVooService provedorVooService;

    private ExecucaoBuscaVooService service;

    @BeforeEach
    void setUp() {
        service = new ExecucaoBuscaVooService(provedorVooFactory);
    }

    @Test
    void deveExecutarFluxoCompletoComSucesso() {
        LocalDate data = LocalDate.of(2026, 12, 10);
        VooDTO vooEsperado = new VooDTO(
                "ViajeMais Air",
                "VM-991",
                "GRU",
                "REC",
                data.atTime(8, 0),
                data.atTime(11, 30),
                new BigDecimal("450.00")
        );

        when(provedorVooFactory.obterProvedor("VIAJEMAIS")).thenReturn(provedorVooService);
        when(provedorVooService.buscarVoos("GRU", "REC", data)).thenReturn(List.of(vooEsperado));

        List<VooDTO> resultado = service.buscar("VIAJEMAIS", "GRU", "REC", data);

        assertThat(resultado).containsExactly(vooEsperado);
        verify(provedorVooFactory).obterProvedor("VIAJEMAIS");
        verify(provedorVooService).buscarVoos("GRU", "REC", data);
    }

    @Test
    void deveLancarExcecaoQuandoOrigemEDestinoForemIguaisSemConsultarProvedor() {
        LocalDate data = LocalDate.of(2026, 12, 10);

        assertThatThrownBy(() -> service.buscar("VIAJEMAIS", "GRU", "GRU", data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Origem e destino não podem ser iguais.");

        verifyNoInteractions(provedorVooFactory, provedorVooService);
    }

    @Test
    void deveLimparThreadLocalAposExcecaoParaNaoVazarEntreChamadas() {
        LocalDate data = LocalDate.of(2026, 12, 10);
        VooDTO vooValido = new VooDTO(
                "ViajeMais Air",
                "VM-1042",
                "GRU",
                "POA",
                data.atTime(15, 20),
                data.atTime(17, 10),
                new BigDecimal("390.00")
        );

        when(provedorVooFactory.obterProvedor("PARCEIRO_INVALIDO"))
                .thenThrow(new IllegalArgumentException("Provedor de voo não encontrado para o código: PARCEIRO_INVALIDO"));

        assertThatThrownBy(() -> service.buscar("PARCEIRO_INVALIDO", "GRU", "POA", data))
                .isInstanceOf(IllegalArgumentException.class);

        // Na mesma thread, uma nova busca válida deve funcionar normalmente sem resquício anterior
        when(provedorVooFactory.obterProvedor("VIAJEMAIS")).thenReturn(provedorVooService);
        when(provedorVooService.buscarVoos("GRU", "POA", data)).thenReturn(List.of(vooValido));

        List<VooDTO> resultado = service.buscar("VIAJEMAIS", "GRU", "POA", data);

        assertThat(resultado).containsExactly(vooValido);
        verify(provedorVooFactory).obterProvedor("VIAJEMAIS");
    }

    @Test
    void deveAgregarVoosDeTodosOsParceirosEOrdenarPorPreco() {
        LocalDate data = LocalDate.of(2026, 12, 10);

        VooDTO vooCaro = new VooDTO("ViajeMais Air", "VM-991", "GRU", "REC",
                data.atTime(8, 0), data.atTime(11, 30), new BigDecimal("450.00"));
        VooDTO vooMaisBarato = new VooDTO("VoaBrasil Linhas Aéreas", "VB-305", "GRU", "REC",
                data.atTime(6, 45), data.atTime(9, 55), new BigDecimal("389.90"));

        ProvedorVooService provedor1 = org.mockito.Mockito.mock(ProvedorVooService.class);
        ProvedorVooService provedor2 = org.mockito.Mockito.mock(ProvedorVooService.class);

        when(provedor1.buscarVoos("GRU", "REC", data)).thenReturn(List.of(vooCaro));
        when(provedor2.buscarVoos("GRU", "REC", data)).thenReturn(List.of(vooMaisBarato));
        when(provedorVooFactory.obterTodosProvedores()).thenReturn(List.of(provedor1, provedor2));

        List<VooDTO> resultado = service.buscarEmTodosParceiros("GRU", "REC", data);

        assertThat(resultado).containsExactly(vooMaisBarato, vooCaro);
    }
}