package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.state.UserState;

import java.util.HashMap;

public class UserInfoManager {
    private final UserState defaultState;
    private final HashMap<UserId, UserInfo> map;

    public UserInfoManager(UserState defaultState) {
        this.defaultState = defaultState;
        map = new HashMap<>();
    }

    public UserInfo getUserInfo(UserId userId) {
        return map.computeIfAbsent(userId, id -> new UserInfo(id, null, null, this.defaultState));
    }

    public void update(UserInfo userInfo) {
        if (userInfo.userId().getFullUserId().isEmpty()) {
            throw new IllegalStateException(
                    "Невозможно обновить данные о пользователе, т.к. отсутствует id в принятых данных"
            );
        }

        map.put(userInfo.userId(), userInfo);
    }
}
