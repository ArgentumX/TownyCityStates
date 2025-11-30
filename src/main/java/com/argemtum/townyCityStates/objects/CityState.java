package com.argemtum.townyCityStates.objects;

import java.util.Map;
import java.util.UUID;

public class CityState {
    private UUID id;
    private String name;

    public CityState(String name){
        this(UUID.randomUUID(), name);
    }

    public CityState(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // TODO extract
    public static CityState fromMap(Map<String, Object> data) {
        return new CityState(
                UUID.fromString((String) data.get("id")),
                (String) data.get("name")
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id.toString(),
                "name", name
        );
    }
}
