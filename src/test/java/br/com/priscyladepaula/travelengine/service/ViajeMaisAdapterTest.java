package br.com.priscyladepaula.travelengine.service;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.service.parceiro.ViajeMaisAdapter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ViajeMaisAdapterTest {

    private final ViajeMaisAdapter adapter = new ViajeMaisAdapter();

    @Test
    void deveRetornarCodigoParceiroCorreto() {
        assertThat(adapter.getCodigoParceiro()).isEqualTo("VIAJEMAIS");
    }

    @Test
    void deveBuscarVoosENormalizarCodigosDeAeroporto() {
        LocalDate data = LocalDate.of(2026, 12, 10);

        List<VooDTO> voos = adapter.buscarVoos("gru", "rec", data);

        assertThat(voos)
                .hasSize(2)
                .extracting(VooDTO::origem, VooDTO::destino)
                .containsOnly(org.assertj.core.groups.Tuple.tuple("GRU", "REC"));

        assertThat(voos.get(0).codigoVoo()).isEqualTo("VM-991");
        assertThat(voos.get(0).horarioPartida()).isEqualTo(data.atTime(8, 0));
        assertThat(voos.get(0).preco()).isEqualByComparingTo(new BigDecimal("450.00"));
    }
}
