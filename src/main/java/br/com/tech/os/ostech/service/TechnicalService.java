package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidNameTechnicalAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidTechnicalIdException;
import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalCreateDTO;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalUpdateDTO;
import br.com.tech.os.ostech.repository.TechnicalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnicalService {

    private final TechnicalRepository technicalRepository;

    public Technical createTechnical(TechnicalCreateDTO technicalCreateDTO) {
        if(technicalRepository.existsByName(technicalCreateDTO.name())) {
            log.error("Technical with name {} already exists", technicalCreateDTO.name());
            throw new InvalidNameTechnicalAlreadyExistsException("Technical already exists");
        }

        Technical technical = new Technical();
        technical.setName(technicalCreateDTO.name());
        technical.setId(UUID.randomUUID().toString());
        log.info("Creating technical {}", technical);

        return technicalRepository.save(technical);
    }

    public Technical updateTechnical(String id, TechnicalUpdateDTO technicalUpdateDTO) {
        if(!technicalRepository.existsById(id)) {
            log.error("Technical with id {} not found", id);
            throw new InvalidTechnicalIdException("Technical not found");
        }
        if(technicalRepository.existsByName(technicalUpdateDTO.name())) {
            log.error("Technical with name {} already exists", technicalUpdateDTO.name());
            throw new InvalidNameTechnicalAlreadyExistsException("Technical already exists");
        }
        Technical technical = technicalRepository.findById(id).orElseThrow(() -> new InvalidTechnicalIdException("Technical not found"));
        technical.setName(technicalUpdateDTO.name());
        log.info("Updating technical {}", technical);

        return technicalRepository.save(technical);
    }

    public void deleteTechnical(String id) {
        if(!technicalRepository.existsById(id)) {
            log.error("Technical with id {} not found", id);
            throw new InvalidTechnicalIdException("Technical not found");
        }
        log.info("Deleting technical with id {}", id);
        technicalRepository.deleteById(id);
    }

    public Technical getTechnicalById(String id) {
        log.info("Getting technical with id {}", id);
        return technicalRepository.findById(id).orElseThrow(() -> new InvalidTechnicalIdException("Technical not found"));
    }

    public List<Technical> getAllTechnicals() {
        log.info("Getting all technicals");
        return technicalRepository.findAll();
    }

}
