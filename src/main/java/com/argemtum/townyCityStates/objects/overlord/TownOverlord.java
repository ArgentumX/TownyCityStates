package com.argemtum.townyCityStates.objects.overlord;

import com.argemtum.townyCityStates.objects.overlord.abstraction.Overlord;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ConfigSerializable
public class TownOverlord extends Overlord {

    private TownOverlord() {}
    public TownOverlord(UUID townId){
        super(townId);
    }

    @Override
    public List<Resident> getRelatedPlayers() {
        return Objects.requireNonNull(TownyAPI.getInstance().getTown(overlordId)).getResidents();
    }

    @Override
    public List<Town> getRelatedTowns() {
        return List.of(Objects.requireNonNull(TownyAPI.getInstance().getTown(overlordId)));
    }
}
