package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.core.Event.UserMessageEvent;
import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.model.UserRequest;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Реализация {@link InputReader} для чтения с консоли
 */
public class ConsoleInputReader implements InputReader {
    private final Scanner scanner;
    public static final String ID_PREFIX = "CONSOLE";

    public ConsoleInputReader(InputStream in) {
        scanner = new Scanner(in);
    }

    @Override
    public UserRequest read() {
        UserId userId = new UserId(ID_PREFIX, "1");

        String line;
        do {
            line =  scanner.nextLine().trim();
        } while (line.isEmpty());

        return new UserRequest(userId, line);
    }
}