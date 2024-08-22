package com.example.exampleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class ExampleprojectApplication {

	// private static Logger log =
	// LoggerFactory.getLogger(ExampleprojectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExampleprojectApplication.class, args);
		log.error("Mensagem error");
		log.warn("Mensagem warn");
		log.info("Mensagem info");
		log.debug("Mensagem debug");
		log.trace("Mensagem trace");
	}
}
