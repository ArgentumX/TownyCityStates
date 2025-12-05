package com.argemtum.townyCityStates.config.language;

import com.argemtum.townyCityStates.config.INode;

public enum MessageNode implements INode {
    PLUGIN_PREFIX("PLUGIN_PREFIX"),
    CITY_CREATED("CITY_CREATED"),
    NO_PERMISSIONS("NO_PERMISSIONS"),
    RELOAD_SETTINGS("RELOAD_SETTINGS"),
    RELOAD_LOCALIZATION("RELOAD_LOCALIZATION"),
    RELOAD_CITIES("RELOAD_CITIES"),
    REGION_NOT_FOUND("REGION_NOT_FOUND"),
    CITY_ALREADY_EXISTS("CITY_ALREADY_EXISTS"),
    REGION_ALREADY_EXISTS("REGION_ALREADY_EXISTS"),
    NOT_PLAYER("NOT_PLAYER"),
    MUST_BE_NUM("MUST_BE_NUM"),
    MUST_BE_POSITIVE("MUST_BE_POSITIVE"),
    CITY_LIST_HEADER("CITY_LIST_HEADER"),
    CITY_LIST_EMPTY("CITY_LIST_EMPTY"),
    CITY_LIST_ENTRY("CITY_LIST_ENTRY"),
    SET_CITY_OVERLORD("SET_CITY_OVERLORD");

    private final String Key;

    MessageNode(String key){
        this.Key = key;
    }

    @Override
    public String getKey() {
        return this.Key;
    }
}
