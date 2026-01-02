package com.corruptedmind.authorizationbot.oauth.dto;

import java.net.URI;

public record DeviceIdResponse(
        String deviceCode,
        String userCode,
        URI verificationURI,
        long expiresIn,
        long interval
) {
}
