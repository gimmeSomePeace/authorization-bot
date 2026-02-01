package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;

public class EventFactory {
    public static Event fromRequest(UserRequest request) {
        return new Event.UserMessageEvent(request.userId(), request.text());
    }
}
