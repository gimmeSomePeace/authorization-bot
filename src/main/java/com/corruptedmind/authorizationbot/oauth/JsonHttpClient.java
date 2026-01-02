package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.exception.HttpRequestException;
import com.corruptedmind.authorizationbot.exception.NetworkException;
import com.corruptedmind.authorizationbot.exception.RequestInterruptedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JsonHttpClient {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);


   public <T> T send(URI uri, Object requestBody, Class<T> responseType) {
        try {
            String json = mapper.writeValueAsString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("BODY: " + response.body());
                throw new HttpRequestException("Запрос не был выполнен успешно. " +
                        response.statusCode() + ", " + response.body());
            }
            return mapper.readValue(response.body(), responseType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Невозможно преобразовать объект в JSON", e);
        } catch (IOException e) {
            throw new NetworkException("Не удалось выполнить HTTP запрос", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestInterruptedException("HTTP запрос был прерван", e);
        }
    }
}
