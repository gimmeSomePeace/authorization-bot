package com.corruptedmind.authorizationbot.oauth.dto;

public record AccessToken(
        String value,
        String tokenType,
        String scope
) {
}
