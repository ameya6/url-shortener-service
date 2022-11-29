package org.shortner.controller;

import org.data.model.ServiceHealth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<ServiceHealth> health() {
        ServiceHealth serviceHealth = ServiceHealth.builder().message("Health").build();
        return ResponseEntity.ok(serviceHealth);
    }
}
