package br.com.priscyladepaula.travelengine.service;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class VoaBrasilAdapter implements ProvedorVooService {

    @Override
    public String getCodigoParceiro() {
        return "VOABRASIL";
    }

    @Override
    public List<VooDTO> buscarVoos(String origem, String destino, LocalDate data) {
        return List.of(
                new VooDTO(
                        "VoaBrasil Linhas Aéreas",
                        "VB-305",
                        origem.toUpperCase(),
                        destino.toUpperCase(),
                        data.atTime(6, 45),
                        data.atTime(9, 55),
                        new BigDecimal("389.90")
                ),
                new VooDTO(
                        "VoaBrasil Linhas Aéreas",
                        "VB-712",
                        origem.toUpperCase(),
                        destino.toUpperCase(),
                        data.atTime(20, 10),
                        data.atTime(23, 20),
                        new BigDecimal("415.00")
                )
        );
    }
}