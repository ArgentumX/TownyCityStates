package com.argemtum.townyCityStates.objects.overlord.abstraction;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public abstract class Overlord {
    @Setting
    protected UUID overlordId;

    protected Overlord() {}
    protected Overlord(UUID overlordId){
        this.overlordId = overlordId;
    }

    public abstract List<Resident> getRelatedPlayers();
    public abstract List<Town> getRelatedTowns();
}
