package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.Event.UserMessageEvent;
import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowSelector;
import com.corruptedmind.authorizationbot.state.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdleStateHandlerTest {
    private UserId userId;
    private UserInfo userInfo;
    private IdleStateHandler handler;

    @BeforeEach
    void setUp() {
        userId = mock(UserId.class);
        userInfo = new UserInfo.Builder().id(userId).build();

        DeviceFlowSelector selector = mock(DeviceFlowSelector.class);
        when(selector.buildMenu()).thenReturn("menu");

        handler = new IdleStateHandler(selector);
    }

    @Test
    void shouldSendMenuAndSwitchUserToLoginState() {
        UserMessageEvent event = new UserMessageEvent(userId, "test-message");

        List<Action> actions = handler.handle(event, userInfo);

        Action.SendMessage sendMessage = findAction(Action.SendMessage.class, actions);
        Action.ChangeUserInfo changeUserInfo = findAction(Action.ChangeUserInfo.class, actions);

        assertEquals(userId, sendMessage.userId());
        assertEquals("menu", sendMessage.message());
        assertEquals(UserState.LOGIN, changeUserInfo.userInfo().state());
        assertEquals(userId, changeUserInfo.userInfo().id());
    }

    @Test
    void shouldThrowExceptionWhenInvalidEventGiven() {
        // TODO: попробовать все же замокать Event
        Event event = new Event.OAuthFailedEvent(userId, "test-message");
        assertThrows(IllegalStateException.class, () -> handler.handle(event, userInfo));
    }

    /**
     * Ищет первый {@code Action} в списке указанного типа
     *
     * @param clazz тип Action, который нужно найти
     * @param actions список Action, возвращаемый обработчиком события
     * @param <T> Тип Action
     * @return Найденный Action нужного типа
     * @throws AssertionError если Action указанного типа не найден
     */
    private <T extends Action> T findAction(Class<T> clazz, List<Action> actions) {
        return actions.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError(clazz.getSimpleName() + " action не найден"));
    }
}
