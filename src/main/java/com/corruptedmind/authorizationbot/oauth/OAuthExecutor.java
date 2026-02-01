package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.core.Action.StartOAuthFlow;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.Event.OAuthFailedEvent;
import com.corruptedmind.authorizationbot.core.Event.OAuthSuccededEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Выполняет асинхронный OAuth Device flow.
 * Executor отвечает за:
 *  - Запуск асинхронного опроса OAuth-сервиса
 *  - Преобразование результата в доменное {@link Event}
 *  - Передачу события во внешний eventSink
 * Все побочные эффекты выражаются исключительно через генерацию {@link Event}
 */
public class OAuthExecutor {
    private final Consumer<Event> eventSink;
    private final Executor executor;

    public OAuthExecutor(Consumer<Event> eventSink, Executor executor) {
        this.eventSink = eventSink;
        this.executor = executor;
    }

    /**
     * Запускает асинхронный полинг OAuth-токена.
     * Метод не блокирует вызывающий поток.
     * Результат выполнения выражается через генерацию доменных событий:
     * <ul>
     *     <li>{@link OAuthSuccededEvent} - при успешном получении токена</li>
     *     <li>{@link OAuthFailedEvent} - при возникновении ошибки</li>
     * </ul>
     * @param action параметры для запуска OAuth Device Flow
     */
    public void execute(StartOAuthFlow action) {
        CompletableFuture
                .supplyAsync(() -> action.service().pollForToken(
                action.deviceCode().deviceCode(),
                action.deviceCode().interval(),
                action.maxAttempts()
                ), executor)
                .thenAccept(token -> eventSink.accept(
                        new OAuthSuccededEvent(action.userId(), token, action.service())
                ))
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    eventSink.accept(
                            new OAuthFailedEvent(action.userId(), cause.getMessage())
                    );
                    return null;
                });
    }
}
