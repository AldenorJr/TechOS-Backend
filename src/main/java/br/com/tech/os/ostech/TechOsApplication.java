package br.com.tech.os.ostech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class TechOsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechOsApplication.class, args);
    }

}
