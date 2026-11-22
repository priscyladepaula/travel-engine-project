package br.com.priscyladepaula.travelengine.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VooDTO(
        String companhia,
        String codigoVoo,
        String origem,
        String destino,
        LocalDateTime horarioPartida,
        LocalDateTime horarioChegada,
        BigDecimal preco
) {}
