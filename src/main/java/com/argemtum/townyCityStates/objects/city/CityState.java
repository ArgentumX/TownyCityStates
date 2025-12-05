package com.argemtum.townyCityStates.objects.city;

import com.argemtum.townyCityStates.objects.city.abstraction.IBonusProvider;
import com.argemtum.townyCityStates.objects.city.abstraction.IInfluenceable;
import com.argemtum.townyCityStates.objects.city.abstraction.IOverlordable;
import com.argemtum.townyCityStates.objects.city.behaviors.CityBonusBehavior;
import com.argemtum.townyCityStates.objects.city.behaviors.CityInfluenceBehavior;
import com.argemtum.townyCityStates.objects.city.behaviors.CityOverlordBehavior;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@ConfigSerializable
public class CityState {
    @Setting
    private UUID id;

    @Setting
    private String name;

    @Setting
    private String regionNameId;

    @Setting
    private CityInfluenceBehavior influenceBehavior;

    @Setting
    private CityBonusBehavior bonusBehavior;

    @Setting
    private CityOverlordBehavior overlordBehavior;

    private CityState() {}

    public CityState(String name) {
        this(name, null);
    }

    public CityState(String name, String regionNameId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.regionNameId = regionNameId;
        influenceBehavior = new CityInfluenceBehavior(this);
        bonusBehavior = new CityBonusBehavior(this);
        overlordBehavior = new CityOverlordBehavior(this);
    }

    public void init(){
        influenceBehavior.init(this);
        bonusBehavior.init(this);
        overlordBehavior.init(this);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getRegionNameId() {
        return Optional.ofNullable(regionNameId);
    }

    public IInfluenceable getInfluenceBehavior() {
        return influenceBehavior;
    }

    public IOverlordable getOverlordBehavior(){
        return overlordBehavior;
    }

    public IBonusProvider getBonusBehavior() {
        return bonusBehavior;
    }
}
