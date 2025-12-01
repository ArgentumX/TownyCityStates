package com.argemtum.townyCityStates.repositories;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Inject;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LocalizationRepository implements ILocalizationRepository{

    private final TownyCityStates plugin;
    private Localization localization;

    @Inject
    public LocalizationRepository(TownyCityStates plugin) {
        this.plugin = plugin;
    }

    @Override
    public Localization GetInstance() {
        if (localization == null) {
            throw new IllegalStateException("Localization not loaded yet. ");
        }
        return localization;
    }

    @Override
    public void load(String path) {
        File languagesDir = new File(plugin.getDataFolder(), "languages");
        languagesDir.mkdirs();

        File file = new File(languagesDir, path);
        if (!file.exists()) {
            plugin.saveResource("languages/" + path, false);
        }

        YamlConfiguration config;
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML configuration for language: " + path, e);
        }
        this.localization = new Localization(config);
        plugin.getLogger().info(String.format("Localization %s loaded from file.", path));
    }
}