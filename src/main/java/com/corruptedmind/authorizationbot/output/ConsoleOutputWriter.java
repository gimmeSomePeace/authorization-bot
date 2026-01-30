package com.corruptedmind.authorizationbot.output;

import com.corruptedmind.authorizationbot.model.UserResponse;


/**
 * Реализация {@link OutputWriter} для вывода в консоль
 */
public class ConsoleOutputWriter implements OutputWriter {

    @Override
    public void write(UserResponse response) {
        System.out.println(response.text());
    }
}