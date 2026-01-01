package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdRequest;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;

public class DeviceAuthService {
    private static final String CLIENT_ID = "Ov23lioD7BEANVIXUfVn";
    private final GitHubRepository authRepository;

    public DeviceAuthService(GitHubRepository authRepository) {
        this.authRepository = authRepository;
    }

    public DeviceIdResponse requestDeviceCode() {
        DeviceIdRequest deviceIdRequest = new DeviceIdRequest(CLIENT_ID);
        return authRepository.requestDeviceCode(deviceIdRequest);
    }
}
