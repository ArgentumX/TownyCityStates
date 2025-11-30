package com.argemtum.townyCityStates.config.language;

import org.bukkit.configuration.file.YamlConfiguration; // Импорт из Bukkit API для работы с YAML

public class Localization {
    private final YamlConfiguration config; // Хранение конфигурации
    public Localization(YamlConfiguration config) {
        this.config = config;
        validateKeys();
    }

    public String of(MessageNode type) {
        return config.getString(type.getKey());
    }

    public String of(MessageNode type, Object... args) {
        String message = of(type);
        return String.format(message, args);
    }

    private void validateKeys() {
        for (MessageNode node : MessageNode.values()) {
            String key = node.getKey();
            if (!config.contains(key)) {
                throw new IllegalStateException("Missing localization key: " + key);
            }
        }
    }
}