package br.com.tech.os.ostech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/v1/status")
    public java.util.Map<String, String> status() {
        return java.util.Collections.singletonMap("status", "online");
    }
    
}
