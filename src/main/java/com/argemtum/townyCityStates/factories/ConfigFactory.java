package com.argemtum.townyCityStates.factories;

import com.argemtum.townyCityStates.config.ConfigNode;
import com.argemtum.townyCityStates.config.ConfigUtils;
import com.argemtum.townyCityStates.config.TownyCityStatesSettings;
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
