package com.corruptedmind.authorizationbot.oauth;

import java.net.URI;

public record OAuthConf(String clientId, URI deviceEndpoint, URI tokenEndpoint, URI userInfoEndpoint) {
}
