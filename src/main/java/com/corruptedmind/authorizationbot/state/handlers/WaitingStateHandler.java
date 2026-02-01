package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Action.SendMessage;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.Event.OAuthFailedEvent;
import com.corruptedmind.authorizationbot.core.Event.OAuthSuccededEvent;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.List;


public class WaitingStateHandler implements UserStateHandler {

    @Override
    public List<Action> handle(Event event, UserInfo userInfo) {
        return switch(event) {
            case Event.UserMessageEvent ignored -> handleUserMessageEvent(userInfo);
            case OAuthSuccededEvent oauthSuccess -> handleOAuthSucceedEvent(oauthSuccess, userInfo);
            case OAuthFailedEvent oauthFailed -> handleOAuthFailedEvent(oauthFailed, userInfo);
        };
    }

    private List<Action> handleUserMessageEvent(UserInfo userInfo) {
        return List.of(
                new SendMessage(
                        userInfo.id(),
                        "Для продолжения работы приложения завершите авторизацию на сервисе"
                )
        );
    }

    private List<Action> handleOAuthSucceedEvent(OAuthSuccededEvent event, UserInfo userInfo) {
        UserInfo newUserInfo = event.service()
                .getUserInfo(event.token())
                .builder()
                .id(userInfo.id())
                .state(UserState.FINISHED)
                .build();
        return List.of(
                new Action.ChangeUserInfo(newUserInfo)
        );
    }

    private List<Action> handleOAuthFailedEvent(OAuthFailedEvent event, UserInfo userInfo) {
        // TODO: make some logic
        return List.of();
    }
}
