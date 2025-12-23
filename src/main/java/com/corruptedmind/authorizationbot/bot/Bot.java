package com.corruptedmind.authorizationbot.bot;

import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.output.OutputWriter;

/**
 * Основной бот, который слушает входящие сообщения и отправляет ответы.
 * Использует InputReader для чтения запросов, LogicCore для обработки и OutputWriter для вывода.
 */
public class Bot {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final LogicCore logicCore;

    public Bot(LogicCore logicCore, InputReader inputReader, OutputWriter outputWriter) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        this.logicCore = logicCore;
    }

    /**
     * Запускает бесконечный цикл обработки сообщений:
     * 1. читает запрос через InputReader
     * 2. обрабатывает его через LogicCore
     * 3. выводит ответ через OutputWriter
     */
    public void listen() {
        while (true) {
            UserRequest userRequest = inputReader.read();
            UserResponse userResponse = logicCore.handle(userRequest);
            outputWriter.write(userResponse);
        }
    }
}
