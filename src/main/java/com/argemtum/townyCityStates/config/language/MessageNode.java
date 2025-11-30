package com.argemtum.townyCityStates.config.language;

import com.argemtum.townyCityStates.config.INode;

public enum MessageNode implements INode {
    CITY_STATE_CREATED("CITY_STATE_CREATED"),
    NO_PERMISSIONS("NO_PERMISSIONS");

    private final String Key;

    MessageNode(String key){
        this.Key = key;
    }

    @Override
    public String getKey() {
        return this.Key;
    }
}
