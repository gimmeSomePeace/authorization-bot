package com.corruptedmind.authorizationbot.oauth.dto;

public record DeviceIdResponse(
        String deviceCode,
        String userCode,
        String verificationURI,
        String expiresIn,
        int interval) {
}
