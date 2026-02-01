package com.corruptedmind.authorizationbot.core;


import java.util.List;

/**
 * Основа приложения. Обрабатывает запросы от пользователя и формирует на их основе ответ.
 */
public interface LogicCore {
    List<Action> handle(Event event);
}