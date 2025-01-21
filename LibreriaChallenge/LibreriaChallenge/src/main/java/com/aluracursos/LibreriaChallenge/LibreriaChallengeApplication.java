package com.aluracursos.LibreriaChallenge;

import com.aluracursos.LibreriaChallenge.principal.Principal;
import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeAutores;
import com.aluracursos.LibreriaChallenge.repositorio.RepositorioDeLibros;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = "com.aluracursos.LibreriaChallenge.modelo")
public class LibreriaChallengeApplication implements CommandLineRunner {

	private final ApplicationContext applicationContext;

	@Autowired
	public LibreriaChallengeApplication(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Autowired
	private RepositorioDeAutores repositorioDeAutores;
	public static void main(String[] args) {

		SpringApplication.run(LibreriaChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = applicationContext.getBean(Principal.class);
		principal.muestraElMenu();
	}
}
