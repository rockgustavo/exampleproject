package com.example.exampleproject.model.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteDeRotasController {

    @GetMapping("/public")
    public ResponseEntity<String> publicRoute() {
        return ResponseEntity.ok("Public route ok!");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateRoute(Authentication authentication) {
        dadosUser(authentication, "Private");
        return ResponseEntity.ok("Private route ok!");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminRoute(Authentication authentication) {
        dadosUser(authentication, "Admin");
        return ResponseEntity.ok("Admin route ok!");
    }

    private void dadosUser(Authentication authentication, String rota) {
        System.out.println("\n****************");
        System.out.println(rota + " route");
        System.out.println("USER: " + authentication.getName());
        System.out.println("ROLES: " + authentication.getAuthorities());
        System.out.println("****************\n");
    }
}
