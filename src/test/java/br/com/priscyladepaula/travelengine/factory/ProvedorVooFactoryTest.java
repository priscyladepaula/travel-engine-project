package br.com.priscyladepaula.travelengine.factory;

import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import br.com.priscyladepaula.travelengine.service.parceiro.ViajeMaisAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProvedorVooFactoryTest {

    private ProvedorVooFactory factory;
    private ViajeMaisAdapter viajeMaisAdapter;

    @BeforeEach
    void setUp() {
        viajeMaisAdapter = new ViajeMaisAdapter();
        List<ProvedorVooService> provedores = List.of(viajeMaisAdapter);
        factory = new ProvedorVooFactory(provedores);
    }

    @Test
    void deveRetornarProvedorCorrespondenteAoCodigo() {
        ProvedorVooService encontrado = factory.obterProvedor("  viajemais ");

        assertThat(encontrado).isSameAs(viajeMaisAdapter);
    }

    @Test
    void deveLancarExcecaoParaParceiroDesconhecido() {
        assertThatThrownBy(() -> factory.obterProvedor("INEXISTENTE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Provedor de voo não encontrado");
    }
}
