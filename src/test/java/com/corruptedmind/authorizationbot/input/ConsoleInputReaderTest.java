package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.model.UserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ConsoleInputReaderTest {
    private static ConsoleInputReader inputReader;

    @BeforeAll
    static void setUp() {
        inputReader = new ConsoleInputReader();
    }

    @Test
    void testRead() {
        // Подготавливаем ввод
        String input = mock(String.class);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserRequest userRequest = inputReader.read();

        assertNotNull(userRequest);
        assertEquals(input, userRequest.text());
    }
}
