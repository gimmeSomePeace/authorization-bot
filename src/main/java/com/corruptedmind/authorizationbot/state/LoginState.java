package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.oauth.DeviceAuthService;
import com.corruptedmind.authorizationbot.oauth.GitHubRepository;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.device.DeviceAuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.device.DeviceCodeGrant;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class LoginState implements UserStateHandler {

    @Override
    public StateHandlerResult handle(UserRequest userRequest) {
        GitHubRepository gitHubRepository = new GitHubRepository();
        DeviceAuthService deviceAuthService = new DeviceAuthService(gitHubRepository);
        DeviceIdResponse responseDeviceCode = deviceAuthService.requestDeviceCode();
        System.out.println(responseDeviceCode);

        try {
            URI verificationURI = new URI(responseDeviceCode.verificationURI());
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(verificationURI);
            }
        } catch (URISyntaxException | IOException e) {
            System.out.println(
                    "Не удалось перейти открыть ссылке в вашем браузере.\n" +
                    "Попробуйте перейти по ссылке вручную: " + responseDeviceCode.verificationURI()
            );
        }

        System.out.println("Ваш код:");
        System.out.println(responseDeviceCode.userCode());

//            AccessToken accessToken = pollForTokens(success);
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

    private AccessToken pollForTokens(DeviceAuthorizationSuccessResponse response) {
        String CLIENT_ID = "Ov23lioD7BEANVIXUfVn";
        URI tokenEndpoint = URI.create("https://github.com/login/oauth/access_token");
        TokenRequest tokenRequest = new TokenRequest(
                tokenEndpoint,
                new ClientID(CLIENT_ID),
                new DeviceCodeGrant(response.getDeviceCode())
        );

        while (true) {
            try {
                HTTPResponse httpResponse = tokenRequest.toHTTPRequest().send();
                TokenResponse tokenResponse = TokenResponse.parse(httpResponse);

                if (tokenResponse.indicatesSuccess()) {
                    return tokenResponse.toSuccessResponse().getTokens().getAccessToken();
                }

                ErrorObject error = tokenResponse.toErrorResponse().getErrorObject();
                if ("authorization_pending".equals(error.getCode())) {
                    Thread.sleep(response.getInterval() * 1000L);
                    continue;
                }

                throw new RuntimeException("Auth error: " + error);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void parseIdToken(Tokens tokens) {


    }
}
