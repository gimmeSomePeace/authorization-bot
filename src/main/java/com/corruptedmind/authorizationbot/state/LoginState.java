package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowProvider;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowService;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowRepository;
import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class LoginState implements UserStateHandler {

    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        OAuthDeviceFlowRepository OAuthDeviceFlowRepository = getRepository(userRequest);
        if (OAuthDeviceFlowRepository == null) return new UserResponse(userInfo.userId(), "Try one more time");

        OAuthDeviceFlowService oAuthDeviceFlowService = new OAuthDeviceFlowService(OAuthDeviceFlowRepository);
        DeviceIdResponse responseDeviceCode = oAuthDeviceFlowService.requestDeviceCode();

        UserResponse response = createResponse(userInfo, responseDeviceCode);
        CompletableFuture<AccessToken> future = requestAccessToken(oAuthDeviceFlowService, responseDeviceCode);
        future.thenAccept(accessToken -> handleAccessToken(accessToken, userInfo, onUserInfoUpdated));

        onUserInfoUpdated.accept(userInfo.builder().state(UserState.WAITING).build());
        return response;
    }

    private UserResponse createResponse(UserInfo userInfo, DeviceIdResponse responseDeviceCode) {
        return new UserResponse(
                userInfo.userId(),
                "Перейдите по адресу и авторизируйтесь: " + responseDeviceCode.verificationURI() +
                        "\nКод подтверждения: " + responseDeviceCode.userCode());
    }

    private CompletableFuture<AccessToken> requestAccessToken(OAuthDeviceFlowService oAuthDeviceFlowService, DeviceIdResponse deviceCode) {
        return CompletableFuture.supplyAsync(() -> oAuthDeviceFlowService.pollForToken(
                deviceCode.deviceCode(),
                deviceCode.interval(),
                1000
        ));
    }

    private void handleAccessToken(AccessToken accessToken, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        String userData = fetchDataFromServer(accessToken);

        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap jsonResponse = mapper.readValue(userData, HashMap.class);
            onUserInfoUpdated.accept(userInfo
                    .builder()
                    .token(accessToken)
                    .state(UserState.FINISHED)
                    .login((String) jsonResponse.get("login"))
                    .build());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Сервер вернул ответ неизвестного формата, невозможно преобразовать в Map: ", e);
        }
    }

    private String fetchDataFromServer(AccessToken accessToken) {
        System.out.println("Access token: " + accessToken.value());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/user"))
                .header("Authorization", "Bearer " + accessToken.value())
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private OAuthDeviceFlowRepository getRepository(UserRequest userRequest) {
        switch (userRequest.text()) {
            case "1" -> {
                return new OAuthDeviceFlowRepository(DeviceFlowProvider.GITHUB.getConf());
            }
            default -> {
                return null;
            }
        }
    }
}
