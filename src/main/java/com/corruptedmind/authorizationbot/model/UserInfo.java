package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.state.UserState;

/**
 * Представляет информацию о пользователе
 * @param id уникальный идентификатор пользователя
 * @param login логин пользователя
 * @param token токен доступа, используемый для аутентификации
 * @param state описывает стадию диалога с пользователем на платформе. Реализовано через паттерн State
 */
public record UserInfo(UserId id, String login, AccessToken token, UserState state) {
    /**
     * Возвращает новый экземпляр {@link Builder}, с помощью которого можно построить объект {@link UserInfo}
     * @return {@link Builder} для создания объекта {@link UserInfo}
     */
    public Builder builder() {
        return new Builder(this);
    }

    /**
     * Реализация паттерна Builder для создания объектов {@link UserInfo}
     */
    public static class Builder {
        private UserId userId;
        private String login;
        private AccessToken accessToken;
        private UserState userState;

        public Builder() {}

        /**
         * Конструктор, создающий новый Builder с начальными значениями из существующего {@link UserInfo}
         * @param userInfo экземпляр {@link UserInfo}, из которого копируются значения
         */
        public Builder(UserInfo userInfo) {
            userId = userInfo.id;
            login = userInfo.login;
            accessToken = userInfo.token;
            userState = userInfo.state;
        }

        public Builder id(UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder login(String userLogin) {
            this.login = userLogin;
            return this;
        }

        public Builder token(AccessToken accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder state(UserState userState) {
            this.userState = userState;
            return this;
        }

        public UserInfo build() {
            return new UserInfo(userId, login, accessToken, userState);
        }
    }
}
