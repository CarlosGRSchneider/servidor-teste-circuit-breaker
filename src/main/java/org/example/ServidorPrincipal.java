package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.circuitos.CircuitBreaker;
import org.example.circuitos.schedulers.CircuitBreakerScheduler;
import org.example.circuitos.variaveis.VariaveisDoCircuito;
import org.example.handlers.devtools.CaosHandler;
import org.example.handlers.GeradorAnimalHandler;
import org.example.handlers.HelloWorldHandler;
import org.example.handlers.devtools.LatenciaHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServidorPrincipal {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        VariaveisDoCircuito variaveisAnimal = new VariaveisDoCircuito(10);
        CircuitBreaker circuitoAnimal = new CircuitBreaker(new GeradorAnimalHandler(variaveisAnimal), variaveisAnimal);

        VariaveisDoCircuito variaveisHW = new VariaveisDoCircuito(20);
        CircuitBreaker circuitoHW = new CircuitBreaker(new HelloWorldHandler(variaveisHW), variaveisHW);


        server.createContext("/animais", circuitoAnimal);
        server.createContext("/hello", circuitoHW);

        server.createContext("/caos", new CaosHandler());
        server.createContext("/latencia", new LatenciaHandler());

        server.setExecutor(null);
        server.start();


        CircuitBreakerScheduler schedulerAnimal = new CircuitBreakerScheduler(circuitoAnimal);
        circuitoAnimal.setCircuitBreakerListener(schedulerAnimal);

        CircuitBreakerScheduler schedulerHW = new CircuitBreakerScheduler(circuitoHW);
        circuitoHW.setCircuitBreakerListener(schedulerHW);

        Runtime.getRuntime().addShutdownHook(new Thread(schedulerAnimal::shutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(schedulerHW::shutdown));

        System.out.println("Servidor iniciado na porta 8080");

    }
}
