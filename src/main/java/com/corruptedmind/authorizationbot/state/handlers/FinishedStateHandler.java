package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Action.ChangeUserInfo;
import com.corruptedmind.authorizationbot.core.Action.SendMessage;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.List;

public class FinishedStateHandler implements UserStateHandler {
    private final String quitCommand;

    public FinishedStateHandler(String quitCommand) {
        this.quitCommand = quitCommand;
    }

    @Override
    public List<Action> handle(Event event, UserInfo userInfo) {
        return switch(event) {
            case Event.UserMessageEvent msg -> handleUserMessageEvent(msg, userInfo);
            default -> throw new IllegalStateException("Невозможно обработать событие в данном state: " + event);
        };
    }

    private List<Action> handleUserMessageEvent(Event.UserMessageEvent event, UserInfo userInfo) {
        if (event.message().equals(quitCommand)) {
            return List.of(
                    new ChangeUserInfo(new UserInfo(userInfo.id(), null, null, UserState.IDLE)),
                    new SendMessage(userInfo.id(), "Вы успешно вышли из аккаунта")
            );
        }

        return List.of(
            new SendMessage(
                userInfo.id(),
                String.format(
                        "Здравствуйте, %s! Вы авторизованы в системе. Для выхода из аккаунта введите %s",
                        userInfo.login(), quitCommand
                )
            )
        );
    }
}
