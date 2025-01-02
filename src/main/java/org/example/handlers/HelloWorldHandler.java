package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.circuitos.variaveis.VariaveisDoCircuito;
import org.example.variaveis.VariaveisDaAplicacao;

import java.io.IOException;
import java.io.OutputStream;

public class HelloWorldHandler implements HttpHandler {

    private VariaveisDoCircuito variaveisDoCircuito;

    public HelloWorldHandler(VariaveisDoCircuito variaveisDoCircuito) {
        this.variaveisDoCircuito = variaveisDoCircuito;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        long agora = System.currentTimeMillis();

        try {
            Thread.sleep(VariaveisDaAplicacao.getLatencia());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String response = "Hello world!";
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody();) {
            os.write(response.getBytes());
        }

        long depois = System.currentTimeMillis();

        if (depois - agora > 1000) {
            variaveisDoCircuito.novaMetricaDeErro();
        }
    }
}