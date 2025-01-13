package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.circuitos.variaveis.VariaveisDoCircuito;
import org.example.variaveis.VariaveisDaAplicacao;

import java.io.IOException;
import java.io.OutputStream;

public class CalculadoraComplexaHandler implements HttpHandler {

    private VariaveisDoCircuito variaveisDoCircuito;

    public CalculadoraComplexaHandler(VariaveisDoCircuito variaveisDoCircuito) {
        this.variaveisDoCircuito = variaveisDoCircuito;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        long agora = System.currentTimeMillis();
        aplicaLatencia();

        String response;

        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            String[] params = body.split("&");

            int numeroUm = Integer.parseInt(params[0].split("=")[1]);
            int numeroDois = Integer.parseInt(params[1].split("=")[1]);

            int sum = numeroUm + numeroDois;
            response = "Soma complexa: " + sum;

            exchange.sendResponseHeaders(200, response.length());
        } catch (Exception e) {
            response = "Entrada invalida. Enviar os seguintes parametros: numero1 e numero2.";
            exchange.sendResponseHeaders(400, response.length());
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }

        long depois = System.currentTimeMillis();
        if (depois - agora > 1000) {
            variaveisDoCircuito.novaMetricaDeErro();
        }
    }

    private void aplicaLatencia() {

        try {
            Thread.sleep(VariaveisDaAplicacao.getLatencia());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
