package com.example.exampleproject.rest.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.exampleproject.model.entity.Cliente;
import com.example.exampleproject.model.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Map<String, Object>> getClienteById(@PathVariable("codigo") Integer id, Authentication auth) {
        Cliente cliente = repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));

        // Criando um Map para armazenar a resposta
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Usuario_Autenticado", auth.getName());
        response.put("cliente", cliente);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> find(Cliente filtro, Authentication auth) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<Cliente> example = Example.of(filtro, matcher);
        List<Cliente> clientes = repository.findAll(example);

        // Criando um Map para armazenar a resposta
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Usuario_Autenticado", auth.getName());
        response.put("clientes", clientes);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save(@Valid @RequestBody Cliente cliente) {
        return repository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> update(@Valid @PathVariable Integer id,
            @RequestBody Cliente cliente) {
        return repository
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    repository.save(cliente);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Cliente atualizado com sucesso!");
                    return response;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> delete(@PathVariable Integer id) {
        return repository.findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Cliente deletado com sucesso!");
                    return response;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

}
