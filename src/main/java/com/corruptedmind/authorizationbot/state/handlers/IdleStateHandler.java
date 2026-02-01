package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Action.ChangeUserInfo;
import com.corruptedmind.authorizationbot.core.Action.SendMessage;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.Event.UserMessageEvent;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowSelector;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.List;


/**
 * Начальное состояние пользователя.
 *
 * <p>Используется для новых пользователей или пользователей,
 * находящихся в состоянии ожидания начала диалога.</p>
 *
 * <p>
        Задачи:
 *     <ul>
 *         <li>Начать диалог с пользователем</li>
 *         <li>Предложить выбрать сервис для OAuth-аутентификации</li>
 *         <li>Перевести пользователя в состояние {@link UserState#LOGIN}</li>
 *     </ul>
 * </p>
 * <p>Обрабатывает только {@link UserMessageEvent}.
 * Остальные события считаются некорректными для данного состояния.</p>
 */
public class IdleStateHandler implements UserStateHandler {
    private final DeviceFlowSelector selector;

    public IdleStateHandler(DeviceFlowSelector selector) {
        this.selector = selector;
    }

    /**
     * Обрабатывает входящее событие в рамках текущего состояния
     *
     * <p>В состоянии {@link UserState#IDLE} поддерживается только {@link UserMessageEvent}.
     * При получении сообщения от пользователя предлагается выбрать сервис
     * для OAuth-аутентификации, после чего пользователь переводится
     * в состояние {@link UserState#LOGIN}. Сообщение, написанное пользователем, игнорируется</p>
     *
     * @param event событие, которое нужно обработать
     * @param userInfo текущая информация о пользователе
     * @return Список {@link Action}, описывающих результат обработки события
     * @throws IllegalStateException если тип события не поддерживается
     */
    @Override
    public List<Action> handle(Event event, UserInfo userInfo) {
        return switch(event) {
            case UserMessageEvent ignored -> handleUserMessageEvent(userInfo);
            default -> throw new IllegalStateException("Невозможно обработать событие в данном state: " + event);
        };
    }

    private List<Action> handleUserMessageEvent(UserInfo userInfo) {
        String menu = selector.buildMenu();
        return List.of(
                new SendMessage(userInfo.id(), menu),
                new ChangeUserInfo(userInfo.builder().state(UserState.LOGIN).build())
        );
    }
}
