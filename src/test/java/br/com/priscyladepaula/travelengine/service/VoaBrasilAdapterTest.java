package br.com.priscyladepaula.travelengine.service;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VoaBrasilAdapterTest {

    private final VoaBrasilAdapter adapter = new VoaBrasilAdapter();

    @Test
    void deveRetornarCodigoParceiroVoaBrasil() {
        assertThat(adapter.getCodigoParceiro()).isEqualTo("VOABRASIL");
    }

    @Test
    void deveRetornarVoosPadronizadosParaVooDTO() {
        LocalDate data = LocalDate.of(2026, 12, 10);

        List<VooDTO> voos = adapter.buscarVoos("gru", "rec", data);

        assertThat(voos).hasSize(2);
        assertThat(voos.get(0).companhia()).isEqualTo("VoaBrasil Linhas Aéreas");
        assertThat(voos.get(0).codigoVoo()).isEqualTo("VB-305");
        assertThat(voos.get(0).origem()).isEqualTo("GRU");
        assertThat(voos.get(0).destino()).isEqualTo("REC");
        assertThat(voos.get(0).preco()).isEqualByComparingTo(new BigDecimal("389.90"));
    }
}
