package org.example.handlers.devtools;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.variaveis.VariaveisDaAplicacao;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LatenciaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String response = "Método não permitido. Somente POST é valido";
            sendResponse(exchange, 405, response);
            return;
        }

        try {
            byte[] requestBody = exchange.getRequestBody().readAllBytes();
            String body = new String(requestBody, StandardCharsets.UTF_8).trim();

            int latencia;
            try {
                latencia = Integer.parseInt(body);
                if (latencia < 0) {
                    throw new NumberFormatException("Latência não pode ser negativa");
                }
            } catch (NumberFormatException e) {
                String response = "Valor de latência inválido. Somente são aceitos números inteiros positivos";
                sendResponse(exchange, 400, response);
                return;
            }

            VariaveisDaAplicacao.setLatencia(latencia);

            String response = "Latência definida com sucesso para " + latencia + "ms";
            sendResponse(exchange, 200, response);

        } catch (IOException e) {
            String response = "Erro ao processar a requisição: " + e.getMessage();
            sendResponse(exchange, 500, response);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}