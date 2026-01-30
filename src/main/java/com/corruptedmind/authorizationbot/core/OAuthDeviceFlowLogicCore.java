package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.*;

/**
 * Это ядро выполняет основную задачу — организует диалог с пользователем,
 * в ходе которого осуществляется аутентификация через один из доступных сервисов
 * с использованием стандартного потока Device Flow.
 */
public class OAuthDeviceFlowLogicCore implements LogicCore {
    // объект, необходимый для хранения и изменения данных о пользователе
    private final UserInfoManager userInfoManager;

    public OAuthDeviceFlowLogicCore(UserInfoManager userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    /**
     * Основной метод, который обрабатывает запрос от пользователя и формирует ответ
     * @param userRequest запрос пользователя
     * @return ответ пользователю
     */
    @Override
    public UserResponse handle(UserRequest userRequest) {
        UserInfo userInfo = userInfoManager.getUserInfo(userRequest.userId());
        return userInfo.state().handle(userRequest, userInfo, this::onUserInfoUpdate);
    }

    /**
     * Этот метод используется как callback: он передается обработчикам состояний и они, в свою очередь,
     * при необходимости, вызывают этот метод для изменения данных о пользователе
     * (в основном это изменение состояния диалога, внесение access token и тд)
     * @param userInfo новые данные о пользователе.
     * @throws IllegalArgumentException - пропущено обязательно поле - id
     */
    private void onUserInfoUpdate(UserInfo userInfo) {
        // Единственное обязательное поле — это поле id, которое указывает,
        // для какого пользователя необходимо внести изменения. Без этого поля
        // остальные данные не могут быть обработаны.
        if (userInfo.id() == null) {
            throw new IllegalArgumentException("Обязательное поле 'id' не указано." +
                    " Невозможно внести изменения в данные пользователя.");
        }
        userInfoManager.update(userInfo);
    }
}