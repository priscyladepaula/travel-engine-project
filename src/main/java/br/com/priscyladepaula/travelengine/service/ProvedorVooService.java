package br.com.priscyladepaula.travelengine.service;

import br.com.priscyladepaula.travelengine.dto.VooDTO;

import java.time.LocalDate;
import java.util.List;

public interface ProvedorVooService {
    String getCodigoParceiro();

    List<VooDTO> buscarVoos(String origem, String destino, LocalDate data);
}
