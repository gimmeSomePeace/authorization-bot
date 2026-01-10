package com.corruptedmind.authorizationbot.oauth.exception;

public record OAuthError(
        String code,
        String description
) {
}
