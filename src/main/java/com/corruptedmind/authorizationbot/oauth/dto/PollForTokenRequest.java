package com.corruptedmind.authorizationbot.oauth.dto;

public record PollForTokenRequest(String clientId, String deviceCode) {
}
