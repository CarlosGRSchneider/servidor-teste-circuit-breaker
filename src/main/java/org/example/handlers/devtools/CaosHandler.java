package org.example.handlers.devtools;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.variaveis.VariaveisDaAplicacao;

import java.io.IOException;
import java.io.OutputStream;

public class CaosHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Boolean estadoDaChave = VariaveisDaAplicacao.alteraEstadoDaChaveDoCaos();

        exchange.sendResponseHeaders(200, estadoDaChave.toString().length());
        try(OutputStream os = exchange.getResponseBody()) {
            os.write(estadoDaChave.toString().getBytes());
        }
    }
}
