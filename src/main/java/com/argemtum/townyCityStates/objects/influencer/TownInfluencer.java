package com.argemtum.townyCityStates.objects.influencer;

import com.argemtum.townyCityStates.objects.influencer.abstraction.Influencer;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Objects;

@ConfigSerializable
public class TownInfluencer extends Influencer {
    private TownInfluencer() {}
}
