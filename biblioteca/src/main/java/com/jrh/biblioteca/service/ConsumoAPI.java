package com.jrh.biblioteca.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {
    public String getData(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("HTTP Response: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la solicitud HTTP: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return response.body();
    }
}
