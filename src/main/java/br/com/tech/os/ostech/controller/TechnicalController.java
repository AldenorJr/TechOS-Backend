package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalCreateDTO;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalUpdateDTO;
import br.com.tech.os.ostech.service.TechnicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/technicals")
public class TechnicalController {

    private final TechnicalService technicalService;

    @PostMapping()
    public ResponseEntity<Technical> createTechnical(@RequestBody TechnicalCreateDTO technicalCreateDTO) {
        Technical createdTechnical = technicalService.createTechnical(technicalCreateDTO);
        return ResponseEntity.status(201).body(createdTechnical);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technical> updateTechnical(@PathVariable String id,
                                                     @RequestBody TechnicalUpdateDTO technicalUpdateDTO) {
        Technical updatedTechnical = technicalService.updateTechnical(id, technicalUpdateDTO);
        return ResponseEntity.ok(updatedTechnical);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technical> getTechnicalById(@PathVariable String id) {
        Technical technical = technicalService.getTechnicalById(id);
        return ResponseEntity.ok(technical);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnical(@PathVariable String id) {
        technicalService.deleteTechnical(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<Technical>> getAllTechnicals() {
        List<Technical> technicals = technicalService.getAllTechnicals();
        return ResponseEntity.ok(technicals);
    }

}
