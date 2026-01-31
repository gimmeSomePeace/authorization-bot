package com.corruptedmind.authorizationbot.app;


import com.corruptedmind.authorizationbot.bot.Bot;
import com.corruptedmind.authorizationbot.core.OAuthDeviceFlowLogicCore;
import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.ConsoleInputReader;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.model.UserInfoManager;
import com.corruptedmind.authorizationbot.output.ConsoleOutputWriter;
import com.corruptedmind.authorizationbot.output.OutputWriter;
import com.corruptedmind.authorizationbot.state.*;
import com.corruptedmind.authorizationbot.state.handlers.FinishedStateHandler;
import com.corruptedmind.authorizationbot.state.handlers.IdleStateHandler;
import com.corruptedmind.authorizationbot.state.handlers.LoginStateHandler;
import com.corruptedmind.authorizationbot.state.handlers.WaitingStateHandler;

import java.util.EnumMap;

/**
 * Главный класс приложения. Отвечает за инициализацию всех компонентов и запуск бота
 */
public class Main {

    /**
     * Формирует полное соответствие между состояниями пользователя и их обработчиками.
     * Все состояния enum {@link UserState} должны быть обработаны.
     * @return EnumMap с обработчиками для всех состояний
     */
    private static EnumMap<UserState, UserStateHandler> fullStateMap() {
        EnumMap<UserState, UserStateHandler> userStateMap = new EnumMap<>(UserState.class);
        for (UserState state: UserState.values()) {
            userStateMap.put(state, switch (state) {
                case IDLE -> new IdleStateHandler();
                case LOGIN -> new LoginStateHandler();
                case WAITING -> new WaitingStateHandler();
                case FINISHED -> new FinishedStateHandler();
            });
        }
        return userStateMap;
    }

    /**
     * Инициализирует основное логическое ядро приложения и его зависимости
     * @return экземпляр LogicCore с полной конфигурацией
     */
    private static LogicCore initializeLogicCore() {
        // Инициализация менеджера информации о пользователе
        UserInfoManager userInfoManager = new UserInfoManager(UserState.IDLE);

        // Инициализация маршрутизатора состояний пользователя
        EnumMap<UserState, UserStateHandler> userStateMap = fullStateMap();
        UserStateRouter stateRouter = new UserStateRouter(userStateMap);

        // Создание основного логического ядра приложения.
        // OAuthDeviceFlowLogicCore реализует логику авторизации на сервисах через OAuth Device Flow
        return new OAuthDeviceFlowLogicCore(userInfoManager, stateRouter);
    }

    /**
     * Точка входа в приложение.
     * Инициализирует все зависимости и запускает цикл обработки сообщений от бота
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Создаем абстракции устройств ввода/вывода
        InputReader inputReader = new ConsoleInputReader(System.in); // Реализация для чтения с консоли
        OutputWriter outputWriter = new ConsoleOutputWriter(); // Реализация для вывода в консоль

        // Инициализируем ядро бота
        LogicCore logicCore = initializeLogicCore();

        // Создаем бота и передаем все зависимости
        Bot bot = new Bot(logicCore, inputReader, outputWriter);
        // Входим в состояние ожидания сообщений от пользователей
        bot.listen();
    }
}
