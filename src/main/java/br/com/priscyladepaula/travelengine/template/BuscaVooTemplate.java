package br.com.priscyladepaula.travelengine.template;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public abstract class BuscaVooTemplate {private static final Logger log = LoggerFactory.getLogger(BuscaVooTemplate.class);

    public final List<VooDTO> executarBusca(String origem, String destino, LocalDate data) {
        validarParametros(origem, destino, data);

        List<VooDTO> resultados = consultarProvedor(origem, destino, data);

        posProcessarResultados(resultados);

        return resultados;
    }

    protected void validarParametros(String origem, String destino, LocalDate data) {
        if (origem == null || origem.isBlank() || destino == null || destino.isBlank() || data == null) {
            throw new IllegalArgumentException("Origem, destino e data são obrigatórios.");
        }
        if (origem.equalsIgnoreCase(destino)) {
            throw new IllegalArgumentException("Origem e destino não podem ser iguais.");
        }
    }

    protected abstract List<VooDTO> consultarProvedor(String origem, String destino, LocalDate data);

    protected void posProcessarResultados(List<VooDTO> resultados) {
        log.info("Busca finalizada. Total de voos encontrados: {}", resultados.size());
    }
}
