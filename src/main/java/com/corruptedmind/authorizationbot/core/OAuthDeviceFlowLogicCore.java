package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.core.Action.ChangeUserInfo;
import com.corruptedmind.authorizationbot.core.Action.DomainAction;
import com.corruptedmind.authorizationbot.model.*;
import com.corruptedmind.authorizationbot.state.UserStateRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * Это ядро выполняет основную задачу — организует диалог с пользователем,
 * в ходе которого осуществляется аутентификация через один из доступных сервисов
 * с использованием стандартного потока Device Flow.
 */
public class OAuthDeviceFlowLogicCore implements LogicCore {
    private final UserStateRouter stateRouter;
    private final UserInfoManager userInfoManager;

    public OAuthDeviceFlowLogicCore(UserStateRouter stateRouter, UserInfoManager userInfoManager) {
        this.stateRouter = stateRouter;
        this.userInfoManager = userInfoManager;
    }

    /**
     * Основной метод, который обрабатывает запрос от пользователя и формирует ответ
     * @param event произошедшее событие
     * @return действия, необходимые предпринять
     */
    @Override
    public List<Action> handle(Event event) {
        UserId userId = event.userId();
        UserInfo userInfo = userInfoManager.getUserInfo(userId);
        List<Action> actions = stateRouter.getHandler(userInfo.state()).handle(event, userInfo);

        List<Action> notDomainActions = new ArrayList<>();
        for (Action action: actions)  {
            if (action instanceof DomainAction domainAction) execute(domainAction);
            else notDomainActions.add(action);
        }

        return notDomainActions;
    }

    private void execute(DomainAction action) {
        switch (action) {
            case ChangeUserInfo chg -> onChangeUserInfoAction(chg);
        }
    }

    /**
     * Обновляет данные о пользователе.
     * Требует передачи id пользователя, данные которого необходимо обновить
     * @param action новые данные о пользователе.
     * @throws IllegalArgumentException - пропущено обязательно поле - id
     */
    private void onChangeUserInfoAction(ChangeUserInfo action) {
        if (action.userInfo().id() == null) {
            throw new IllegalArgumentException("Обязательное поле 'id' не указано." +
                    " Невозможно внести изменения в данные пользователя.");
        }
        userInfoManager.update(action.userInfo());
    }
}