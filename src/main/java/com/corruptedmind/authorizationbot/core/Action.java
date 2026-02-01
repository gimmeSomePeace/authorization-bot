package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowService;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;

public sealed interface Action permits Action.DomainAction, Action.SendMessage, Action.StartOAuthFlow {
    record StartOAuthFlow(UserId userId, OAuthDeviceFlowService service, DeviceIdResponse deviceCode, int maxAttempts) implements Action {}
    record SendMessage(UserId userId, String message) implements Action {}
    record ChangeUserInfo(UserInfo userInfo) implements DomainAction {}

    sealed interface DomainAction extends Action permits ChangeUserInfo {}
}
