package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.classes.GeradorAnimal;
import org.example.circuitos.variaveis.VariaveisDoCircuito;
import org.example.variaveis.VariaveisDaAplicacao;

import java.io.IOException;
import java.io.OutputStream;

public class GeradorAnimalHandler implements HttpHandler {

    private final GeradorAnimal geradorAnimal = new GeradorAnimal();
    private final VariaveisDoCircuito variaveisDoCircuito;

    public GeradorAnimalHandler(VariaveisDoCircuito variaveisDeLatencia) {
        this.variaveisDoCircuito = variaveisDeLatencia;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (VariaveisDaAplicacao.isChaveDoCaos()) {
            boolean erro = VariaveisDaAplicacao.isChaveDeException();

            if (erro) {
                variaveisDoCircuito.novaMetricaDeErro();
                responseDeErro(exchange, "Falha no gerador!", 400);
                return;
            }
        }

        String response = geradorAnimal.geraCombinacao();
        responseDeSucesso(exchange, response);
    }

    private void responseDeErro(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }

    private void responseDeSucesso(HttpExchange exchange, String response) throws IOException {
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}