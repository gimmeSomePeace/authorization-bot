package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowService;
import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;

public sealed interface Event permits Event.OAuthFailedEvent, Event.OAuthSuccededEvent, Event.UserMessageEvent {
    UserId userId();

    record UserMessageEvent(UserId userId, String message) implements Event {}
    record OAuthSuccededEvent(UserId userId, AccessToken token, OAuthDeviceFlowService service) implements Event {}
    record OAuthFailedEvent(UserId userId, String message) implements Event {}
}
