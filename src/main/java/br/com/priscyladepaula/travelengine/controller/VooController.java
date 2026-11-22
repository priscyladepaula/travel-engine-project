package br.com.priscyladepaula.travelengine.controller;

import br.com.priscyladepaula.travelengine.dto.VooDTO;
import br.com.priscyladepaula.travelengine.factory.ProvedorVooFactory;
import br.com.priscyladepaula.travelengine.service.ProvedorVooService;
import br.com.priscyladepaula.travelengine.template.ExecucaoBuscaVooService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/voos")
public class VooController {

    private final ExecucaoBuscaVooService execucaoBuscaVooService;

    public VooController(ExecucaoBuscaVooService execucaoBuscaVooService) {
        this.execucaoBuscaVooService = execucaoBuscaVooService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<VooDTO>> buscarVoos(
            @RequestParam String parceiro,
            @RequestParam String origem,
            @RequestParam String destino,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<VooDTO> voos = execucaoBuscaVooService.buscar(parceiro, origem, destino, data);
        return ResponseEntity.ok(voos);
    }

    @GetMapping("/buscar-todos")
    public ResponseEntity<List<VooDTO>> buscarTodosVoos(
            @RequestParam String origem,
            @RequestParam String destino,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<VooDTO> voos = execucaoBuscaVooService.buscarEmTodosParceiros(origem, destino, data);
        return ResponseEntity.ok(voos);
    }
}
