package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Action.ChangeUserInfo;
import com.corruptedmind.authorizationbot.core.Action.SendMessage;
import com.corruptedmind.authorizationbot.core.Action.StartOAuthFlow;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.core.Event.UserMessageEvent;
import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowProvider;
import com.corruptedmind.authorizationbot.oauth.DeviceFlowSelector;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowService;
import com.corruptedmind.authorizationbot.oauth.OAuthDeviceFlowRepository;
import com.corruptedmind.authorizationbot.oauth.dto.DeviceIdResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.List;
import java.util.Optional;


/**
 * Состояние, в котором пользователь выбирает сервис
 * для авторизации через OAuth Device Flow.
 *
 * <p>Отвечает за:
 * <ul>
 *     <li>Обработку {@link UserMessageEvent}</li>
 *     <li>Проверку выбранного пользователем сервиса</li>
 *     <li>Формирование списка {@link Action}, включая {@link SendMessage}, {@link ChangeUserInfo} и {@link StartOAuthFlow}</li>
 * </ul>
 */
public class LoginStateHandler implements UserStateHandler {
    private final DeviceFlowSelector selector;
    private final int maxAttemptsForPollingToken;

    public LoginStateHandler(DeviceFlowSelector selector, int maxAttemptsForPollingToken) {
        this.selector = selector;
        this.maxAttemptsForPollingToken = maxAttemptsForPollingToken;
    }

    /**
     * Обрабатывает событие пользователя в состоянии LOGIN.
     *
     * <p>Поддерживается только {@link UserMessageEvent}. В случае некорректного
     * события выбрасывается {@link IllegalStateException}.
     *
     * <p>Если пользователь выбрал неподдерживаемый сервис — возвращается
     * {@link SendMessage} с сообщением о неправильном вводе.
     *
     * @param event событие, которое нужно обработать
     * @param userInfo текущее состояние пользователя
     * @return список действий {@link Action}, которые должны быть выполнены после обработки
     */
    @Override
    public List<Action> handle(Event event, UserInfo userInfo) {
        return switch(event) {
            case UserMessageEvent msg -> handleUserMessageEvent(msg, userInfo);
            default -> throw new IllegalStateException("Невозможно обработать событие в данном state: " + event);
        };
    }


    private List<Action> handleUserMessageEvent(UserMessageEvent event, UserInfo userInfo) {
        Optional<OAuthDeviceFlowRepository> repositoryOpt = getRepository(event.message());
        if (repositoryOpt.isEmpty()) return List.of(new SendMessage(userInfo.id(), "Прости.... Я не понимаю в каком сервисе ты хочешь авторизоваться. Попробуй ввести еще раз."));

        OAuthDeviceFlowService service = new OAuthDeviceFlowService(repositoryOpt.get());
        DeviceIdResponse responseDeviceCode = service.requestDeviceCode();

        return buildLoginActions(userInfo, responseDeviceCode, service);
    }


    private List<Action> buildLoginActions(UserInfo userInfo, DeviceIdResponse responseDeviceCode, OAuthDeviceFlowService service) {
        UserInfo newInfo = userInfo.builder().state(UserState.WAITING).build();
        return List.of(
                new SendMessage(
                        userInfo.id(),
                        "Перейдите по адресу и авторизируйтесь: " + responseDeviceCode.verificationURI() +
                                "\nКод подтверждения: " + responseDeviceCode.userCode()
                ),
                new ChangeUserInfo(newInfo),
                new StartOAuthFlow(userInfo.id(), service, responseDeviceCode, maxAttemptsForPollingToken)
        );
    }


    /**
     * Возвращает репозиторий OAuth Device Flow в зависимости от выбранного пользователем сервиса.
     * @param message запрос пользователя, содержащий идентификатор сервиса
     * @return репозиторий для аутентификации через выбранный сервис
     *         или {@code null}, если сервис не поддерживается
     */
    private Optional<OAuthDeviceFlowRepository> getRepository(String message) {
        Optional<DeviceFlowProvider> provider = selector.getProvider(message);
        if (provider.isEmpty()) return Optional.empty();
        return Optional.of(new OAuthDeviceFlowRepository(provider.get().conf()));
    }
}
