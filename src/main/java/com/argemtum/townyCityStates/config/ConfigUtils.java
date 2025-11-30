package com.argemtum.townyCityStates.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtils {
    /**
    * Fills config with default values
    */
    public static void setDefaultFields(YamlConfiguration configuration){
        for (ConfigNode node : ConfigNode.values()) {
            configuration.set(node.getPath(), node.getDefaultValue());
        }
    }

    /**
     *  Return true if config was modified
    */
    public static boolean updateConfigIfNeeded(YamlConfiguration oldConfiguration){
        boolean modified = false;
        for (ConfigNode node : ConfigNode.values()) {
            if (!oldConfiguration.contains(node.getPath())) {
                oldConfiguration.set(node.getPath(), node.getDefaultValue());
                modified = true;
            }
        }
        return modified;
    }
}
