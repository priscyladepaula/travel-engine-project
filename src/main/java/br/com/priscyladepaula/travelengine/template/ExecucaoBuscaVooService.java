package br.com.priscyladepaula.travelengine.template;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.factory.ProvedorVooFactory;
import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ExecucaoBuscaVooService extends BuscaVooTemplate {

    private final ProvedorVooFactory provedorVooFactory;
    private final ThreadLocal<String> parceiroAtual = new ThreadLocal<>();
    private final ThreadLocal<Boolean> buscaAgregada = ThreadLocal.withInitial(() -> false);

    public ExecucaoBuscaVooService(ProvedorVooFactory provedorVooFactory) {
        this.provedorVooFactory = provedorVooFactory;
    }

    public List<VooDTO> buscar(String parceiro, String origem, String destino, LocalDate data) {
        buscaAgregada.set(false);
        parceiroAtual.set(parceiro);
        try {
            return executarBusca(origem, destino, data);
        } finally {
            parceiroAtual.remove();
            buscaAgregada.remove();
        }
    }

    public List<VooDTO> buscarEmTodosParceiros(String origem, String destino, LocalDate data) {
        buscaAgregada.set(true);
        try {
            return executarBusca(origem, destino, data);
        } finally {
            buscaAgregada.remove();
        }
    }

    @Override
    protected List<VooDTO> consultarProvedor(String origem, String destino, LocalDate data) {
        if (Boolean.TRUE.equals(buscaAgregada.get())) {
            return provedorVooFactory.obterTodosProvedores().stream()
                    .flatMap(provedor -> provedor.buscarVoos(origem, destino, data).stream())
                    .sorted(Comparator.comparing(VooDTO::preco))
                    .toList();
        }

        String codigoParceiro = parceiroAtual.get();
        ProvedorVooService provedor = provedorVooFactory.obterProvedor(codigoParceiro);
        return provedor.buscarVoos(origem, destino, data);
    }

    @Override
    protected void posProcessarResultados(List<VooDTO> resultados) {
        super.posProcessarResultados(resultados);
    }
}