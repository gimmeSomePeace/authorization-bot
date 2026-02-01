package com.corruptedmind.authorizationbot.oauth;

import com.corruptedmind.authorizationbot.core.Action.StartOAuthFlow;
import com.corruptedmind.authorizationbot.core.Event;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.corruptedmind.authorizationbot.core.Event.OAuthFailedEvent;
import com.corruptedmind.authorizationbot.core.Event.OAuthSuccededEvent;
import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class OAuthExecutorTest {
    private Consumer<Event> eventSink;
    private OAuthExecutor executor;

    @BeforeEach
    void setUp() {
        eventSink = mock(Consumer.class);
        Executor directExecutor = Runnable::run;
        executor = new OAuthExecutor(eventSink, directExecutor);
    }

    @Test
    void shouldSendSuccessEventWhenTokenReceived() {
        String rawDeviceCode = "device-code";
        long interval = 5L;
        int maxAttemptsForPollingToken = 1000;

        StartOAuthFlow action = mock(StartOAuthFlow.class);
        OAuthDeviceFlowService service = mock(OAuthDeviceFlowService.class);
        DeviceIdResponse deviceCode = mock(DeviceIdResponse.class);
        UserId userId = mock(UserId.class);
        AccessToken token = mock(AccessToken.class);

        when(action.service()).thenReturn(service);
        when(action.deviceCode()).thenReturn(deviceCode);
        when(action.userId()).thenReturn(userId);

        when(deviceCode.deviceCode()).thenReturn(rawDeviceCode);
        when(deviceCode.interval()).thenReturn(interval);

        when(service.pollForToken(rawDeviceCode, interval, maxAttemptsForPollingToken)).thenReturn(token);

        executor.execute(action);

        // Основная проверка
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventSink).accept(captor.capture());
        Event event = captor.getValue();
        assertInstanceOf(OAuthSuccededEvent.class, event);

        OAuthSuccededEvent success = (OAuthSuccededEvent) event;
        assertEquals(userId, success.userId());
        assertEquals(token, success.token());
    }

    @Test
    void shouldSendFailedEventWhenExceptionOccurs() {
        String rawDeviceCode = "device-code";
        String exceptionMessage = "test exception";
        int maxAttemptsForPollingToken = 1000;
        long interval = 5L;

        StartOAuthFlow action = mock(StartOAuthFlow.class);
        OAuthDeviceFlowService service = mock(OAuthDeviceFlowService.class);
        DeviceIdResponse deviceCode = mock(DeviceIdResponse.class);
        UserId userId = mock(UserId.class);

        when(action.service()).thenReturn(service);
        when(action.deviceCode()).thenReturn(deviceCode);
        when(action.userId()).thenReturn(userId);
        when(action.maxAttempts()).thenReturn(maxAttemptsForPollingToken);

        when(deviceCode.deviceCode()).thenReturn(rawDeviceCode);
        when(deviceCode.interval()).thenReturn(interval);

        when(service.pollForToken(any(), anyLong(), anyInt())).thenThrow(new RuntimeException(exceptionMessage));

        executor.execute(action);
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventSink).accept(captor.capture());
        Event event = captor.getValue();
        assertInstanceOf(OAuthFailedEvent.class, event);

        OAuthFailedEvent failed = (OAuthFailedEvent) event;
        assertEquals(userId, failed.userId());
        assertEquals(exceptionMessage, failed.message());
    }
}
