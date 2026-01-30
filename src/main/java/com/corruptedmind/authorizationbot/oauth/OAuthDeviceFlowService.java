package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.oauth.dto.*;
import com.corruptedmind.authorizationbot.state.UserState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;


public class OAuthDeviceFlowService {
    private final OAuthDeviceFlowRepository authRepository;

    public OAuthDeviceFlowService(OAuthDeviceFlowRepository authRepository) {
        this.authRepository = authRepository;
    }

    public DeviceIdResponse requestDeviceCode() {
        return authRepository.requestDeviceCode();
    }

    public AccessToken pollForToken(String deviceCode, long intervalSeconds, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            TokenResult result = authRepository.pollForToken(deviceCode);
            switch (result) {
                case TokenResult.Success success:
                        return success.token();
                case TokenResult.Error error:
                    switch (error.error().code()) {
                        case "authorization_pending" -> sleep(intervalSeconds);
                        case "slow_down" -> sleep(intervalSeconds + 5);
                        case "expired_token" -> throw new IllegalStateException("Device code expired");
                        default -> throw new IllegalStateException("OAuth error: " + error.error().code());
                    }
            }
        }
        throw new IllegalStateException("Polling timeout exceed");
    }

    public UserInfo getUserInfo(AccessToken accessToken) {
        String rawUserInfo = authRepository.getUserInfo(accessToken);

        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap jsonResponse = mapper.readValue(rawUserInfo, HashMap.class);
            return new UserInfo.Builder()
                    .state(UserState.FINISHED)
                    .login((String) jsonResponse.get("login"))
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Сервер вернул ответ неизвестного формата, невозможно преобразовать в Map: ", e);
        }
    }

    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Polling interrupted", e);
        }
    }
}
