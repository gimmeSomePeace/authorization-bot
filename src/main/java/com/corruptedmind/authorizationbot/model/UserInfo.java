package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.state.UserState;

public record UserInfo(UserId userId, String login, AccessToken token, UserState state) {
    public Builder builder() {
        return new Builder(this);
    }

    public static class Builder {
        private UserId userId;
        private String login;
        private AccessToken accessToken;
        private UserState userState;

        public Builder(UserInfo userInfo) {
            userId = userInfo.userId;
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
