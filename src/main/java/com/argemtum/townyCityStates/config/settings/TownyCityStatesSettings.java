package com.argemtum.townyCityStates.config.settings;

import org.bukkit.configuration.file.YamlConfiguration;

public class TownyCityStatesSettings {
    private YamlConfiguration config;

    public TownyCityStatesSettings(YamlConfiguration config){
        this.config = config;
    }

    public YamlConfiguration getConfig(){
        return this.config;
    }

    public String getLocalization(){
        return config.getString(ConfigNode.LANGUAGE.getKey());
    }
}
