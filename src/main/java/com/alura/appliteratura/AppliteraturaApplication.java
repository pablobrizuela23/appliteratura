package com.alura.appliteratura;

import com.alura.appliteratura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppliteraturaApplication implements CommandLineRunner {

    @Autowired
    private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(AppliteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.mostrarMenu();
	}
}
