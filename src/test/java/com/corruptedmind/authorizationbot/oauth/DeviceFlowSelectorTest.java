package com.corruptedmind.authorizationbot.oauth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceFlowSelectorTest {

    @Test
    void shouldIncludeAllProvidersInMenu() {
        OAuthConf gitConf = mock(OAuthConf.class);
        OAuthConf googleConf = mock(OAuthConf.class);

        DeviceFlowProvider[] providers = new DeviceFlowProvider[] {
                new DeviceFlowProvider(gitConf, "GitHub"),
                new DeviceFlowProvider(googleConf, "Google")
        };
        DeviceFlowSelector selector = new DeviceFlowSelector(providers);
        String menu = selector.buildMenu();
        assertTrue(menu.contains("GitHub"));
        assertTrue(menu.contains("Google"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnProviderForEveryIndex(int index) {
        String userMessage = String.valueOf(index);

        OAuthConf gitConf = mock(OAuthConf.class);
        OAuthConf googleConf = mock(OAuthConf.class);

        DeviceFlowProvider[] providers = new DeviceFlowProvider[] {
                new DeviceFlowProvider(gitConf, "GitHub"),
                new DeviceFlowProvider(googleConf, "Google")
        };
        DeviceFlowSelector selector = new DeviceFlowSelector(providers);

        Optional<DeviceFlowProvider> provider = selector.getProvider(userMessage);
        assertTrue(provider.isPresent());
        assertEquals(providers[index - 1], provider.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "3", "999", "test"})
    void shouldReturnEmptyWhenInputIsInvalid(String userInput) {
        OAuthConf gitConf = mock(OAuthConf.class);
        OAuthConf googleConf = mock(OAuthConf.class);

        DeviceFlowProvider[] providers = new DeviceFlowProvider[] {
                new DeviceFlowProvider(gitConf, "GitHub"),
                new DeviceFlowProvider(googleConf, "Google")
        };
        DeviceFlowSelector selector = new DeviceFlowSelector(providers);
        Optional<DeviceFlowProvider> provider = selector.getProvider(userInput);
        assertTrue(provider.isEmpty());
    }
}
