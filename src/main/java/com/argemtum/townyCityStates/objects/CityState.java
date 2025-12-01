package com.argemtum.townyCityStates.objects;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CityState {
    private UUID id;
    private String name;
    private String regionNameId;

    public CityState(String name){
        this(UUID.randomUUID(), name, null);
    }

    public CityState(String name, String regionNameId) {
        this(UUID.randomUUID(), name, regionNameId);
    }

    public CityState(UUID id, String name, String regionNameId) {
        this.id = id;
        this.name = name;
        this.regionNameId = regionNameId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getRegionNameId(){
        return Optional.ofNullable(regionNameId);
    }

    // TODO extract
    public static CityState fromMap(Map<String, Object> data) {
        return new CityState(
                UUID.fromString((String) data.get("id")),
                (String) data.get("name"),
                (String) data.get("regionNameId")
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id.toString(),
                "name", name,
                "regionNameId", regionNameId
        );
    }
}
