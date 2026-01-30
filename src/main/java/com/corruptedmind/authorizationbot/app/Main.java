package com.corruptedmind.authorizationbot.app;


import com.corruptedmind.authorizationbot.bot.Bot;
import com.corruptedmind.authorizationbot.core.OAuthDeviceFlowLogicCore;
import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.ConsoleInputReader;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.model.UserInfoManager;
import com.corruptedmind.authorizationbot.output.ConsoleOutputWriter;
import com.corruptedmind.authorizationbot.output.OutputWriter;
import com.corruptedmind.authorizationbot.state.UserState;

public class Main {
    public static void main(String[] args) {
        // Создаем абстракции устройств ввода/вывода
        InputReader inputReader = new ConsoleInputReader(System.in); // Реализация для чтения с консоли
        OutputWriter outputWriter = new ConsoleOutputWriter(); // Реализация для вывода в консоль

        // Инициализация менеджера информации о пользователе
        UserInfoManager userInfoManager = new UserInfoManager(UserState.IDLE);
        // Создание основного логического ядра приложения.
        // OAuthDeviceFlowLogicCore реализует логику авторизации на сервисах через OAuth Device Flow
        LogicCore logicCore = new OAuthDeviceFlowLogicCore(userInfoManager);

        // Создаем бота и передаем все зависимости через механизм Dependency Injection
        Bot bot = new Bot(logicCore, inputReader, outputWriter);
        // Входим в состояние ожидания сообщений от пользователей
        bot.listen();
    }
}
