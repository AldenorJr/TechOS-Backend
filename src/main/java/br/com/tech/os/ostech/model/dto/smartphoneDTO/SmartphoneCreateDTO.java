package br.com.tech.os.ostech.model.dto.smartphoneDTO;

public record SmartphoneCreateDTO(String model, String problem, String observation, String serial) {
    
    public SmartphoneCreateDTO {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null or blank");
        }
        if (problem == null || problem.isBlank()) {
            throw new IllegalArgumentException("Problem cannot be null or blank");
        }
        if (observation == null || observation.isBlank()) {
            throw new IllegalArgumentException("Observation cannot be null or blank");
        }
        if (serial == null || serial.isBlank()) {
            throw new IllegalArgumentException("Serial cannot be null or blank");
        }
    }

}
