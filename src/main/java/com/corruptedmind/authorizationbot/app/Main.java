package com.corruptedmind.authorizationbot.app;


import com.corruptedmind.authorizationbot.bot.Bot;
import com.corruptedmind.authorizationbot.core.AuthorizationLogicCore;
import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.ConsoleInputReader;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.output.ConsoleOutputWriter;
import com.corruptedmind.authorizationbot.output.OutputWriter;

public class Main {
    public static void main(String[] args) {
        LogicCore logicCore = new AuthorizationLogicCore();
        InputReader inputReader = new ConsoleInputReader();
        OutputWriter outputWriter = new ConsoleOutputWriter();

        Bot bot = new Bot(logicCore, inputReader, outputWriter);
        bot.listen();
    }
}
