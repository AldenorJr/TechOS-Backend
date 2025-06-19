package br.com.tech.os.ostech.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.tech.os.ostech.model.Smartphone;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneCreateDTO;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneUpdateDTO;
import br.com.tech.os.ostech.service.SmartphoneService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/smartphone")
public class SmartphoneController {

    private final SmartphoneService smartphoneService;

    @PostMapping()
    public ResponseEntity<Smartphone> createSmartphone(@RequestBody SmartphoneCreateDTO smartphoneCreateDTO) {
        Smartphone smartphone = smartphoneService.createSmartphone(smartphoneCreateDTO);
        return ResponseEntity.status(201).body(smartphone);
    }

    @PutMapping("/{smartphoneId}")
    public ResponseEntity<Smartphone> updateSmartphone(@PathVariable String smartphoneId,
                                                 @RequestBody SmartphoneUpdateDTO smartphoneUpdateDTO) {
        Smartphone smartphone = smartphoneService.updateSmartphone(smartphoneId, smartphoneUpdateDTO);
        return ResponseEntity.ok(smartphone);
    }

    @DeleteMapping("/{smartphoneId}")
    public ResponseEntity<Void> deleteSmartphone(@PathVariable String smartphoneId) {
        smartphoneService.deleteSmartphone(smartphoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Smartphone> getContactById(@PathVariable String clientId) {
        Smartphone smartphone = smartphoneService.getSmartphoneById(clientId);
        return ResponseEntity.ok(smartphone);
    }

}
