package br.com.tech.os.ostech.service;

import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;
import br.com.tech.os.ostech.exception.InvalidSmartphoneIdException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Smartphone;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneCreateDTO;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneUpdateDTO;
import br.com.tech.os.ostech.repository.SmartphoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    public Smartphone createSmartphone(SmartphoneCreateDTO smartphoneCreateDTO) {
        log.info("Creating smartphone with model: {}", smartphoneCreateDTO.model());
        Smartphone smartphone = new Smartphone();
        smartphone.setModel(smartphoneCreateDTO.model());
        smartphone.setProblem(smartphoneCreateDTO.problem());
        smartphone.setObservation(smartphoneCreateDTO.observation());
        smartphone.setSerial(smartphoneCreateDTO.serial());
        smartphone.setId(UUID.randomUUID().toString());
        smartphone.setCreatedAt(new Date());
        smartphone.setUpdatedAt(new Date());

        return smartphoneRepository.save(smartphone);
    }

    public Smartphone updateSmartphone(String smartphoneId, SmartphoneUpdateDTO smartphoneUpdateDTO) {
        log.info("Updating smartphone with ID: {}", smartphoneId);
        Smartphone smartphone = smartphoneRepository.findById(smartphoneId)
                .orElseThrow(() -> new InvalidSmartphoneIdException("Smartphone not found"));

        smartphone.setModel(smartphoneUpdateDTO.model());
        smartphone.setProblem(smartphoneUpdateDTO.problem());
        smartphone.setSerial(smartphoneUpdateDTO.serial());
        smartphone.setObservation(smartphoneUpdateDTO.observation());
        smartphone.setUpdatedAt(new Date());


        log.info("Updated smartphone with ID: {} to model: {}", smartphoneId, smartphone.getModel());
        return smartphoneRepository.save(smartphone);
    }

    public void deleteSmartphone(String smartphoneId) {
        log.info("Deleting smartphone with ID: {}", smartphoneId);
        Smartphone smartphone = smartphoneRepository.findById(smartphoneId)
                .orElseThrow(() -> new InvalidSmartphoneIdException("Smartphone not found"));
        smartphoneRepository.delete(smartphone);
        log.info("Deleted smartphone with ID: {}", smartphoneId);
    }

    public Smartphone getSmartphoneById(String smartphoneId) {
        log.info("Fetching smartphone with ID: {}", smartphoneId);
        return smartphoneRepository.findById(smartphoneId)
                .orElseThrow(() -> new InvalidSmartphoneIdException("Smartphone not found"));
    }

}
