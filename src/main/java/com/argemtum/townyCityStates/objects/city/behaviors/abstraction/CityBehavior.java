package com.argemtum.townyCityStates.objects.city.behaviors.abstraction;

import com.argemtum.townyCityStates.objects.city.CityState;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public abstract class CityBehavior {
    protected transient CityState cityState;
    public void init(CityState cityState){
        this.cityState = cityState;
    }
}
