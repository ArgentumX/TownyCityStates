package com.argemtum.townyCityStates.config;

public enum ConfigNode {

    LANGUAGE(
            "language.language",
            "en-US.yml"
    );

    private final String Path;
    private final String DefaultValue;

     ConfigNode(String root, String defaultVal) {
        this.Path = root;
        this.DefaultValue = defaultVal;
    }

    public String getPath() {
        return Path;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }
}
