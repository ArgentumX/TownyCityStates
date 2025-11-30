package com.argemtum.townyCityStates.repositories;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.config.ConfigUtils;
import com.argemtum.townyCityStates.config.TownyCityStatesSettings;
import com.argemtum.townyCityStates.factories.ConfigFactory;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.google.inject.Inject;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigYamlRepository implements IConfigRepository {
    private final ConfigFactory configFactory;
    private final TownyCityStates plugin; // Добавляем инъекцию плагина для доступа к dataFolder
    private TownyCityStatesSettings settings;
    private File configFile;

    @Inject
    public ConfigYamlRepository(ConfigFactory configFactory, TownyCityStates plugin){
        this.configFactory = configFactory;
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "settings.yml");
    }
    @Override
    public TownyCityStatesSettings GetInstance() {
        if (settings == null) {
            throw new IllegalStateException("Settings not loaded");
        }
        return settings;
    }

    @Override
    public void load() {
        if (!configFile.exists()) {
            settings = configFactory.generateDefaultSetting();
            save();
            plugin.getLogger().info("Default config created and saved.");
        } else {
            YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(configFile);
            settings = new TownyCityStatesSettings(yamlConfig);
            plugin.getLogger().info("Config loaded from file.");
            if (ConfigUtils.updateConfigIfNeeded(settings.getConfig())) {
                save();
                plugin.getLogger().info("Config updated with new defaults.");
            }
        }
    }

    @Override
    public void save() {
        if (settings == null) {
            throw new IllegalStateException("Settings not initialized.");
        }
        YamlConfiguration config = settings.getConfig();
        try {
            config.save(configFile);
            plugin.getLogger().info("Config saved.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config: " + e.getMessage());
        }
    }
}
