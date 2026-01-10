package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.*;

public class OAuthDeviceFlowLogicCore implements LogicCore {
    private final UserInfoManager userInfoManager;

    public OAuthDeviceFlowLogicCore(UserInfoManager userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    @Override
    public UserResponse handle(UserRequest userRequest) {
        UserInfo userInfo = userInfoManager.getUserInfo(userRequest.userId());
        return userInfo.state().handle(userRequest, userInfo, this::onUserInfoUpdate);
    }

    private void onUserInfoUpdate(UserInfo userInfo) {
        userInfoManager.update(userInfo);
    }
}