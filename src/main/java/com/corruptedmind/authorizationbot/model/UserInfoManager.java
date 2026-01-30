package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.state.UserState;
import java.util.HashMap;

/**
 * Сервис для получения и изменения информации о пользователе.
 */
public class UserInfoManager {
    // Состояние диалога для новых пользователей
    private final UserState defaultState;
    private final HashMap<UserId, UserInfo> map;

    public UserInfoManager(UserState defaultState) {
        this.defaultState = defaultState;
        map = new HashMap<>();
    }

    /**
     * Возвращает данные о пользователе согласно его id.
     * @param userId id пользователя
     * @return данные о пользователе
     */
    public UserInfo getUserInfo(UserId userId) {
        // Для новых пользователей создаем новую запись со значениями по умолчанию
        return map.computeIfAbsent(userId, id -> new UserInfo.Builder().id(id).state(defaultState).build());
    }

    /**
     * Обновляет данные о пользователе. Единственный инвариант - id пользователя не должен быть пуст.
     * @param userInfo новые данные о пользователе
     * @throws IllegalArgumentException обязательное поле id равно null
     */
    public void update(UserInfo userInfo) {
        if (userInfo.id() == null) {
            throw new IllegalArgumentException(
                    "Невозможно обновить данные пользователя, так как отсутствует обязательное поле 'id'"
            );
        }

        map.put(userInfo.id(), userInfo);
    }
}
