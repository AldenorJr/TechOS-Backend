package br.com.tech.os.ostech.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "TechOS API", version = "1.0", description = "Documentação da API da Tech OS")
)
@SpringBootApplication
public class SwaggerUi {



}
