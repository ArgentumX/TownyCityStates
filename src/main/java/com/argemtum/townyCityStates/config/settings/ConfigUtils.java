package com.argemtum.townyCityStates.config.settings;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtils {
    /**
    * Fills config with default values
    */
    public static void setDefaultFields(YamlConfiguration configuration){
        for (ConfigNode node : ConfigNode.values()) {
            configuration.set(node.getKey(), node.getDefaultValue());
        }
    }

    /**
     *  Return true if config was modified
    */
    public static boolean updateConfigIfNeeded(YamlConfiguration oldConfiguration){
        boolean modified = false;
        for (ConfigNode node : ConfigNode.values()) {
            if (!oldConfiguration.contains(node.getKey())) {
                oldConfiguration.set(node.getKey(), node.getDefaultValue());
                modified = true;
            }
        }
        return modified;
    }
}
