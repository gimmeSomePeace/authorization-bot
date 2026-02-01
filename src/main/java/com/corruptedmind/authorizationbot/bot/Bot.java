package com.corruptedmind.authorizationbot.bot;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.EventFactory;
import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.oauth.OAuthExecutor;
import com.corruptedmind.authorizationbot.output.OutputWriter;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Основной бот, который слушает входящие сообщения и отправляет ответы.
 * Использует InputReader для чтения запросов, LogicCore для обработки и OutputWriter для вывода.
 */
public class Bot {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final LogicCore logicCore;

    private final OAuthExecutor oAuthExecutor;

    public Bot(
            LogicCore logicCore,
            InputReader inputReader,
            OutputWriter outputWriter
    ) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        this.logicCore = logicCore;
        this.oAuthExecutor = new OAuthExecutor(this::onEvent, Executors.newCachedThreadPool());
    }

    /**
     * Запускает бесконечный цикл обработки сообщений:
     * 1. читает запрос через InputReader
     * 2. обрабатывает его через LogicCore
     * 3. выводит ответ через OutputWriter
     */
    public void listen() {
        while (true) {
            UserRequest request = inputReader.read();
            Event event = EventFactory.fromRequest(request);

            List<Action> actions = logicCore.handle(event);
            actions.forEach(this::execute);
        }
    }

    private void execute(Action action) {
        switch (action) {
            case Action.StartOAuthFlow flowAction -> executeStartOAthFlowAction(flowAction);
            case Action.SendMessage msgAction -> executeSendMessageAction(msgAction);
            default -> throw new IllegalStateException("Bot не может обрабатывать action: " + action);
        }
    }

    private void executeStartOAthFlowAction(Action.StartOAuthFlow flowAction) {
        oAuthExecutor.execute(flowAction);
    }

    private void executeSendMessageAction(Action.SendMessage action) {
        UserResponse response = new UserResponse(action.userId(), action.message());
        outputWriter.write(response);
    }

    private void onEvent(Event event) {
        List<Action> actions = logicCore.handle(event);
        actions.forEach(this::execute);
    }
}
