package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowProvider;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowService;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowRepository;
import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


/**
 * Состояние, в котором пользователь выбирает сервис
 * для авторизации через OAuth Device Flow.
 */
public class LoginStateHandler implements UserStateHandler {

    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        // Обрабатываем запрос с целью выяснить в каком сервисе хочет авторизоваться пользователь
        Optional<OAuthDeviceFlowRepository> repositoryOpt = getRepository(userRequest);
        // В случае неудачи выводим пользователю соответствующий ответ
        if (repositoryOpt.isEmpty()) return new UserResponse(userInfo.id(), "Прости.... Я не понимаю в каком сервисе ты хочешь авторизоваться. Попробуй ввести еще раз.");

        // Создаем объект, который предоставляет интерфейс для авторизации
        OAuthDeviceFlowService service = new OAuthDeviceFlowService(repositoryOpt.get());
        // Получаем device code [1 шаг для авторизации через механизм device flow]
        DeviceIdResponse responseDeviceCode = service.requestDeviceCode();

        // формируем ответ для пользователя
        UserResponse response = createResponse(userInfo, responseDeviceCode);
        // Запускаем в другом потоке асинхронный полинг токена
        CompletableFuture<AccessToken> future = requestAccessToken(service, responseDeviceCode);
        // устанавливаем callback, который будет вызван при авторизации пользователя и получении access token
        future.thenAccept(accessToken -> handleAccessToken(accessToken, userInfo, onUserInfoUpdated, service));

        // Изменяем активное состояние пользователя
        onUserInfoUpdated.accept(userInfo.builder().state(UserState.WAITING).build());
        return response;
    }

    private UserResponse createResponse(UserInfo userInfo, DeviceIdResponse responseDeviceCode) {
        return new UserResponse(
                userInfo.id(),
                "Перейдите по адресу и авторизируйтесь: " + responseDeviceCode.verificationURI() +
                        "\nКод подтверждения: " + responseDeviceCode.userCode());
    }

    private CompletableFuture<AccessToken> requestAccessToken(OAuthDeviceFlowService service, DeviceIdResponse deviceCode) {
        return CompletableFuture.supplyAsync(() -> service.pollForToken(
                deviceCode.deviceCode(),
                deviceCode.interval(),
                1000
        ));
    }

    private void handleAccessToken(AccessToken accessToken, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated, OAuthDeviceFlowService oAuthDeviceFlowService) {
        UserInfo newUserInfo = oAuthDeviceFlowService.getUserInfo(accessToken).builder().id(userInfo.id()).build();
        onUserInfoUpdated.accept(newUserInfo);
    }

    /**
     * Возвращает репозиторий OAuth Device Flow в зависимости от выбранного пользователем сервиса.
     * @param userRequest запрос пользователя, содержащий идентификатор сервиса
     * @return репозиторий для аутентификации через выбранный сервис
     *         или {@code null}, если сервис не поддерживается
     */
    private Optional<OAuthDeviceFlowRepository> getRepository(UserRequest userRequest) {
        return switch (userRequest.text()) {
            case "1" -> Optional.of(
                    new OAuthDeviceFlowRepository(
                            DeviceFlowProvider.GITHUB.getConf()
                    )
            );
            default -> Optional.empty();
        };
    }
}
