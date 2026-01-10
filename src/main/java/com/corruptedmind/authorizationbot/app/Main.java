package com.corruptedmind.authorizationbot.app;


import com.corruptedmind.authorizationbot.bot.Bot;
import com.corruptedmind.authorizationbot.core.OAuthDeviceFlowLogicCore;
import com.corruptedmind.authorizationbot.core.LogicCore;
import com.corruptedmind.authorizationbot.input.ConsoleInputReader;
import com.corruptedmind.authorizationbot.input.InputReader;
import com.corruptedmind.authorizationbot.model.UserInfoManager;
import com.corruptedmind.authorizationbot.output.ConsoleOutputWriter;
import com.corruptedmind.authorizationbot.output.OutputWriter;
import com.corruptedmind.authorizationbot.state.UserState;

public class Main {
    public static void main(String[] args) {
        InputReader inputReader = new ConsoleInputReader();
        OutputWriter outputWriter = new ConsoleOutputWriter();

        UserInfoManager userInfoManager = new UserInfoManager(UserState.IDLE);
        LogicCore logicCore = new OAuthDeviceFlowLogicCore(userInfoManager);

        Bot bot = new Bot(logicCore, inputReader, outputWriter);
        bot.listen();
    }
}
