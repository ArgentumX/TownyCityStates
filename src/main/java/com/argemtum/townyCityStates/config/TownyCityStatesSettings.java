package com.argemtum.townyCityStates.config;

import com.argemtum.townyCityStates.TownyCityStates;
import org.bukkit.configuration.file.YamlConfiguration;

public class TownyCityStatesSettings {
    private YamlConfiguration config;

    public TownyCityStatesSettings(YamlConfiguration config){
        this.config = config;
    }

    public YamlConfiguration getConfig(){
        return this.config;
    }
}
