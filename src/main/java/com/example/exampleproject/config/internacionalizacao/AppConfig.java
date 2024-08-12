package com.example.exampleproject.config.internacionalizacao;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@ProductionConfig
public class AppConfig {

    @Bean
    public CommandLineRunner executar() {
        return args -> {
            System.out.println("Configuração de produção");
        };
    }

}
