package com.corruptedmind.authorizationbot.oauth.dto;

public record OAuthError(
        String code,
        String description
) {
}
