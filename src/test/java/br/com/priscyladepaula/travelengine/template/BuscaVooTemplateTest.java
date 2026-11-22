package br.com.priscyladepaula.travelengine.template;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BuscaVooTemplateTest {

    @Test
    void deveExecutarFluxoCompletoQuandoParametrosForemValidos() {
        AtomicBoolean consultouProvedor = new AtomicBoolean(false);

        BuscaVooTemplate template = new BuscaVooTemplate() {
            @Override
            protected List<VooDTO> consultarProvedor(String origem, String destino, LocalDate data) {
                consultouProvedor.set(true);
                return List.of(new VooDTO(
                        "Teste Air",
                        "TS-100",
                        origem,
                        destino,
                        data.atTime(10, 0),
                        data.atTime(12, 0),
                        new BigDecimal("300.00")
                ));
            }
        };

        LocalDate data = LocalDate.of(2026, 12, 15);
        List<VooDTO> resultado = template.executarBusca("GRU", "CGH", data);

        assertThat(consultouProvedor).isTrue();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveLancarExcecaoQuandoOrigemEDestinoForemIguais() {
        BuscaVooTemplate template = new BuscaVooTemplate() {
            @Override
            protected List<VooDTO> consultarProvedor(String origem, String destino, LocalDate data) {
                throw new AssertionError("Não deveria consultar o provedor se a validação falhar");
            }
        };

        assertThatThrownBy(() -> template.executarBusca("GRU", "gru", LocalDate.of(2026, 12, 15)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Origem e destino não podem ser iguais.");
    }

    @Test
    void deveLancarExcecaoQuandoParametroObrigatorioEstiverVazio() {
        BuscaVooTemplate template = new BuscaVooTemplate() {
            @Override
            protected List<VooDTO> consultarProvedor(String origem, String destino, LocalDate data) {
                return List.of();
            }
        };

        assertThatThrownBy(() -> template.executarBusca("", "REC", LocalDate.of(2026, 12, 15)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Origem, destino e data são obrigatórios.");
    }
}
