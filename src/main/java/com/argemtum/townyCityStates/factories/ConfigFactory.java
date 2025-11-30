package com.argemtum.townyCityStates.factories;

import com.argemtum.townyCityStates.config.settings.ConfigUtils;
import com.argemtum.townyCityStates.config.settings.TownyCityStatesSettings;
import com.argemtum.townyCityStates.factories.abstraction.IConfigFactory;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFactory implements IConfigFactory {
    @Override
    public TownyCityStatesSettings generateDefaultSetting() {
        YamlConfiguration defaultConfig = new YamlConfiguration();
        ConfigUtils.setDefaultFields(defaultConfig);
        return new TownyCityStatesSettings(defaultConfig);
    }
}
