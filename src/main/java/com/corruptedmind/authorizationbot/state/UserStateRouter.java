package com.corruptedmind.authorizationbot.state;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Маршрутизатор обработчиков пользовательских состояний.
 * Хранит соответствие между {@link UserState} и {@link UserStateHandler}.
 * Гарантирует, что обработчик задан для каждого возможного состояния.
 * Проверка полноты маппинга выполняется при инициализации. В случае отсутствия обработчиков для одного
 * или нескольких состояний выбрасывается {@link IllegalStateException}.
 * Класс является неизменяемым после создания
 */
public class UserStateRouter {
    private final EnumMap<UserState, UserStateHandler> stateToHandler;

    /**
     * Создает маршрутизатор состояний пользователя
     * @param stateToHandler отображение состояний в обработчики;
     *                       Должно содержать обработчик для каждого {@link UserState}
     * @throws IllegalStateException если отображение не содержит обработчики для всех возможных состояний
     * @throws NullPointerException если {@code stateToHandler} равен {@code null}
     */
    public UserStateRouter(EnumMap<UserState, UserStateHandler> stateToHandler) {
        Objects.requireNonNull(
                stateToHandler,
                "StateToHandler не может быть null"
        );
        this.stateToHandler = new EnumMap<>(stateToHandler);

        // Проверяем, что для всех состояний заданы обработчики. В ином случае - исключение
        EnumSet<UserState> missing = EnumSet.allOf(UserState.class);
        missing.removeAll(stateToHandler.keySet());
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Ошибка инициализации UserStateRouter: не заданы обработчики для состояний " + missing);
        }
    }

    /**
     * Возвращает обработчик для указанного состояния пользователя
     * @param state состояние пользователя
     * @return обработчик, соответствующий состоянию
     * @throws NullPointerException если обработчик для данного состояния отсутствует
     */
    public UserStateHandler getHandler(UserState state) {
        return Objects.requireNonNull(
                stateToHandler.get(state),
                "Не задан обработчик для состояния " + state
        );
    }
}
