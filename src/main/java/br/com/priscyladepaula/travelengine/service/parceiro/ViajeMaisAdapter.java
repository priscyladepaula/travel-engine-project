package br.com.priscyladepaula.travelengine.service.parceiro;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
public class ViajeMaisAdapter implements ProvedorVooService {

    @Override
    public String getCodigoParceiro() {
        return "VIAJEMAIS";
    }

    @Override
    public List<VooDTO> buscarVoos(String origem, String destino, LocalDate data) {
        return List.of(
                new VooDTO(
                        "ViajeMais Air",
                        "VM-991",
                        origem.toUpperCase(),
                        destino.toUpperCase(),
                        data.atTime(8,0),
                        data.atTime(11,30),
                        new BigDecimal("450.00")

                ),
                new VooDTO(
                        "ViajeMais Air",
                        "VM-1042",
                        origem.toUpperCase(),
                        destino.toUpperCase(),
                        data.atTime(15,20),
                        data.atTime(18,50),
                        new BigDecimal("520.00")
                )
        );
    }
}
