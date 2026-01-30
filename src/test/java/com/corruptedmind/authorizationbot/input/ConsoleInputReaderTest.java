package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.model.UserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleInputReaderTest {
    private static ConsoleInputReader inputReader;

    @BeforeAll
    static void setUp() {
    }

    @Test
    void testRead() {
        // Подготавливаем ввод
        String input = "test-value";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        inputReader = new ConsoleInputReader(in);

        UserRequest userRequest = inputReader.read();

        assertNotNull(userRequest);
        assertEquals(input, userRequest.text());
    }
}
