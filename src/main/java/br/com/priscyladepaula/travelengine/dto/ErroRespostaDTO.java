package br.com.priscyladepaula.travelengine.dto;

import java.time.LocalDateTime;

public record ErroRespostaDTO(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp
) {
}
