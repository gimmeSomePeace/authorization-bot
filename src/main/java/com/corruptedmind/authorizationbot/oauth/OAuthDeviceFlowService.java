package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.oauth.dto.*;


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

    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Polling interrupted", e);
        }
    }
}
