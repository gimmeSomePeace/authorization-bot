package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.model.UserRequest;

import java.util.Scanner;

public class ConsoleInputReader implements InputReader {
    private final Scanner scanner;
    public static final String ID_PREFIX = "CONSOLE";

    public ConsoleInputReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public UserRequest read() {
        String userId = ID_PREFIX + "_" + "1";

        String line;
        do {
            line =  scanner.nextLine().trim();
        } while (line.isEmpty());
        return new UserRequest(userId, line);
    }
}