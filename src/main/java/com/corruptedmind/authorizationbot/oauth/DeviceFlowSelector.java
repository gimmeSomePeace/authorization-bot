package com.corruptedmind.authorizationbot.oauth;

import java.util.Optional;

/**
 * Интерпретирует пользовательский ввод как выбор сервиса для OAuth-авторизации.
 *
 * <p>Отвечает за:
 * <ul>
 *     <li>Формирование меню выбора сервиса</li>
 *     <li>Интерпретация пользовательского ввода как выбор сервиса</li>
 * </ul>
 * </p>
 *
 * <p>Класс не выполняет авторизацию и не содержит бизнес-логики OAuth,
 * а отвечает исключительно за выбор сервиса на основе пользовательского ввода</p>
 */
public class DeviceFlowSelector {
    private final DeviceFlowProvider[] providers;

    public DeviceFlowSelector(DeviceFlowProvider[] providers) {
        this.providers = providers;
    }

    /**
     * Формирует текстовое меню доступных сервисов для OAuth-авторизации
     * <p>Пункты меню нумеруются, начиная с 1, в порядке, соответствующему переданному списку {@code providers}.</p>
     * @return строковое представление меню для отображения пользователю
     */
    public String buildMenu() {
        StringBuilder menu = new StringBuilder("Здравствуйте! Выберите сервис, через который хотите авторизоваться:\n");
        for (int i = 0; i < providers.length; i++) {
            menu.append("    ")
                    .append(i + 1)
                    .append(") ")
                    .append(providers[i].displayName())
                    .append("\n");
        }
        return menu.toString();
    }

    /**
     * Интерпретирует пользовательский ввод как выбор сервиса
     * @param message ввод пользователя
     * @return выбранный {@link DeviceFlowProvider} или {@link Optional#empty()},
     *                   если ввод не соответствует ни одному сервису
     */
    public Optional<DeviceFlowProvider> getProvider(String message) {
        try {
            int index = Integer.parseInt(message) - 1;
            if (index >= 0 && index < providers.length) {
                return Optional.of(providers[index]);
            }
        } catch (NumberFormatException ignored)  {}
        return Optional.empty();
    }
}
