package com.corruptedmind.authorizationbot.oauth;

import java.net.URI;

public enum DeviceFlowProvider {
    GITHUB(new OAuthConf(
            "Ov23lioD7BEANVIXUfVn",
            URI.create("https://github.com/login/device/code"),
            URI.create("https://github.com/login/oauth/access_token")
    ));

    private final OAuthConf conf;

    DeviceFlowProvider(OAuthConf conf) {
        this.conf = conf;
    }

    public OAuthConf getConf() {
        return conf;
    }
}
