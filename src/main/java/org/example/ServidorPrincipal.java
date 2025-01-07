package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.circuitos.CircuitBreaker;
import org.example.circuitos.schedulers.CircuitBreakerScheduler;
import org.example.circuitos.variaveis.VariaveisDoCircuito;
import org.example.handlers.CalculadoraComplexaHandler;
import org.example.handlers.GeradorAnimalHandler;
import org.example.handlers.devtools.CaosHandler;
import org.example.handlers.devtools.LatenciaHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServidorPrincipal {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        VariaveisDoCircuito variaveisAnimal = new VariaveisDoCircuito(10);
        CircuitBreaker circuitoAnimal = new CircuitBreaker(new GeradorAnimalHandler(variaveisAnimal), variaveisAnimal);

        VariaveisDoCircuito variaveisCalculadora = new VariaveisDoCircuito(20);
        CircuitBreaker circuitoCalculadora = new CircuitBreaker(new CalculadoraComplexaHandler(variaveisCalculadora), variaveisCalculadora);


        server.createContext("/animais", circuitoAnimal);
        server.createContext("/calculadora", circuitoCalculadora);

        server.createContext("/dev-tools/caos", new CaosHandler());
        server.createContext("/dev-tools/latencia", new LatenciaHandler());

        server.setExecutor(null);
        server.start();


        CircuitBreakerScheduler schedulerAnimal = new CircuitBreakerScheduler(circuitoAnimal);
        circuitoAnimal.setCircuitBreakerListener(schedulerAnimal);

        CircuitBreakerScheduler schedulerCalculadora = new CircuitBreakerScheduler(circuitoCalculadora);
        circuitoCalculadora.setCircuitBreakerListener(schedulerCalculadora);

        Runtime.getRuntime().addShutdownHook(new Thread(schedulerAnimal::shutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(schedulerCalculadora::shutdown));

        System.out.println("Servidor iniciado na porta 8080");
    }
}