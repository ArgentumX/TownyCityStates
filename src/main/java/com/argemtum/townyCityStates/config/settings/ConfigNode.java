package com.argemtum.townyCityStates.config.settings;

import com.argemtum.townyCityStates.config.INode;

public enum ConfigNode implements INode {

    LANGUAGE(
            "language.language",
            "en-US.yml"
    );

    private final String Key;
    private final String DefaultValue;

     ConfigNode(String root, String defaultVal) {
        this.Key = root;
        this.DefaultValue = defaultVal;
    }

    public String getKey() {
        return Key;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }
}
