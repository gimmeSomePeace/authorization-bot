package com.corruptedmind.authorizationbot.oauth;


import com.corruptedmind.authorizationbot.exception.HttpRequestException;
import com.corruptedmind.authorizationbot.exception.NetworkException;
import com.corruptedmind.authorizationbot.exception.RequestInterruptedException;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdRequest;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class GitHubRepository {
    private static final URI DEVICE_ENDPOINT = URI.create("https://github.com/login/device/code");
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);


    private String toJsonString(DeviceIdRequest deviceIdRequest) {
         try {
             return mapper.writeValueAsString(deviceIdRequest);
         } catch (JsonProcessingException e) {
             throw new IllegalStateException("Невозможно преобразовать объект в JSON", e);
         }
    }

//    private Map<String, String> parseHtmlFormToMap(String responseBody) {
//         Map<String, String> result = new HashMap<>();
//         for (String s: responseBody.split("&")) {
//             String[] parts = s.split("=", 2);
//             if (parts.length == 2)
//                 result.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
//                            URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
//         }
//         return result;
//    }

    private String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new HttpRequestException("Запрос не был выполнен успешно. " +
                                                response.statusCode() + ", " + response.body());
            }
            return response.body();
        } catch (IOException e) {
            throw new NetworkException("Не удалось выполнить HTTP запрос", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestInterruptedException("HTTP запрос был прерван", e);
        }
    }

    public DeviceIdResponse requestDeviceCode(DeviceIdRequest deviceIdRequest) {
        String requestBodyAsJson = toJsonString(deviceIdRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(DEVICE_ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyAsJson))
                .build();

        String responseJson = sendRequest(request);
        try {
            DeviceIdResponse deviceIdResponse = mapper.readValue(responseJson, DeviceIdResponse.class);
            return deviceIdResponse;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось обработать ответ, полученный от сервера", e);
        }
    }
}
