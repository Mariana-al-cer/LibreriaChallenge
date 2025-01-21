package com.aluracursos.LibreriaChallenge.servicio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class Configuracion {

        @Bean
        public ObjectMapper ObjectMapper(){
            return new ObjectMapper();
        }

        @Bean
        public Scanner teclado(){
            return new Scanner(System.in);
        }

}
