package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.oauth.DeviceAuthService;
import com.corruptedmind.authorizationbot.oauth.GitHubRepository;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;

import java.awt.*;
import java.io.IOException;


public class LoginState implements UserStateHandler {

    @Override
    public StateHandlerResult handle(UserRequest userRequest) {
        GitHubRepository gitHubRepository = new GitHubRepository();
        DeviceAuthService deviceAuthService = new DeviceAuthService(gitHubRepository);
        DeviceIdResponse responseDeviceCode = deviceAuthService.requestDeviceCode();
        System.out.println(responseDeviceCode);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(responseDeviceCode.verificationURI());
            }
        } catch (IOException e) {
            System.out.println(
                    "Не удалось открыть ссылку в вашем браузере.\n" +
                    "Попробуйте перейти по ссылке вручную: " + responseDeviceCode.verificationURI()
            );
        }

        System.out.println("Ваш код:");
        System.out.println(responseDeviceCode.userCode());

        com.corruptedmind.authorizationbot.oauth.dto.AccessToken response = deviceAuthService.pollForToken(responseDeviceCode.deviceCode(), responseDeviceCode.interval(), 1000);
        System.out.println("TOKEN: " + response.value());

//            System.out.println("Access token: " + accessToken.getValue());
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("https://api.github.com/user"))
//                    .header("Authorization", "Bearer" + accessToken.getValue())
//                    .header("Accept", "application/json")
//                    .GET()
//                    .build();
//
//            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            System.out.println("Данные пользователя: ");
//            System.out.println(httpResponse.body());

        return null;

    }
}
