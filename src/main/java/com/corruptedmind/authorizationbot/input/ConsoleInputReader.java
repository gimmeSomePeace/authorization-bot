package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.model.UserRequest;

import java.util.Scanner;

public class ConsoleInputReader implements InputReader {
    private final Scanner scanner;

    public ConsoleInputReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public UserRequest read() {
        String line;
        do {
            line =  scanner.nextLine().trim();
        } while (line.isEmpty());
        return new UserRequest(line);
    }
}