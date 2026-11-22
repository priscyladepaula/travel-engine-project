package br.com.priscyladepaula.travelengine.factory;

import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProvedorVooFactory {

    private final Map<String, ProvedorVooService> provedoresPorCodigo;

    public ProvedorVooFactory(List<ProvedorVooService> provedores) {
        this.provedoresPorCodigo = provedores.stream()
                .collect(Collectors.toMap(
                        provedor -> provedor.getCodigoParceiro().toUpperCase(),
                        Function.identity()
                ));
    }

    public ProvedorVooService obterProvedor(String codigoParceiro) {
        if (codigoParceiro == null || codigoParceiro.isBlank()) {
            throw new IllegalArgumentException("O código do parceiro é obrigatório.");
        }

        ProvedorVooService provedor = provedoresPorCodigo.get(codigoParceiro.trim().toUpperCase());
        if (provedor == null) {
            throw new IllegalArgumentException("Provedor de voo não encontrado para o código: " + codigoParceiro);
        }

        return provedor;
    }

    public List<ProvedorVooService> obterTodosProvedores() {
        return List.copyOf(provedoresPorCodigo.values());
    }
}
