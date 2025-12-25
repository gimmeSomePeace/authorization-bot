package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserId;

import java.util.HashMap;


/**
 * Контейнер, который хранит активные состояния пользователей.
 * Может лишь выдавать активные состояния, либо менять их
 */
public class UserStateManager {
    private final HashMap<String, UserState> activeStates;

    public UserStateManager() {
        activeStates = new HashMap<>();
    }

    /**
     * @param userId id пользователя, состояние которого хотим узнать
     * @return возвращает состояние пользователя или, в случае если
     *         пользователь не был зарегистрирован в системе,
     *         возвращаем состояние приветствия
     */
    public UserState getActiveState(UserId userId) {
        return activeStates.getOrDefault(userId.getFullUserId(), UserState.IDLE);
    }

    public void updateActiveState(UserId userId, UserState newState) {
        activeStates.put(userId.getFullUserId(), newState);
    }
}
