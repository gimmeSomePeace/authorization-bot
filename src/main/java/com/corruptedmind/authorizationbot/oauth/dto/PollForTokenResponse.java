package com.corruptedmind.authorizationbot.oauth.dto;

public record PollForTokenResponse(String accessToken, String tokenType, String scope, String error, String errorDescription, String errorUri) {
}
