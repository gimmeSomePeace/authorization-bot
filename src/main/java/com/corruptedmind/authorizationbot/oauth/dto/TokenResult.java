package com.corruptedmind.authorizationbot.oauth.dto;

import com.corruptedmind.authorizationbot.oauth.exception.OAuthError;

public sealed interface TokenResult permits TokenResult.Success, TokenResult.Error {
    record Success(AccessToken token) implements TokenResult {}
    record Error(OAuthError error) implements TokenResult {}
}
