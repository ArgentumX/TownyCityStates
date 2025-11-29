package com.argemtum.townyCityStates.objects;

import java.util.UUID;

public class CityState {
    private UUID id;
    private String name;

    public CityState(String name) {
        id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
