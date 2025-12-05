package com.argemtum.townyCityStates.objects.city.behaviors;

import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.objects.city.abstraction.IOverlordable;
import com.argemtum.townyCityStates.objects.city.behaviors.abstraction.CityBehavior;
import com.argemtum.townyCityStates.objects.overlord.abstraction.Overlord;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import javax.annotation.Nullable;
import java.util.Optional;

@ConfigSerializable
public class CityOverlordBehavior extends CityBehavior implements IOverlordable {

    @Setting
    @Nullable
    private Overlord overlord;

    private CityOverlordBehavior(){}
    public CityOverlordBehavior(CityState cityState){
        init(cityState);
    }

    @Override
    public Optional<Overlord> getOverlord() {
        return Optional.ofNullable(overlord);
    }

    @Override
    public void setOverlord(@NotNull Overlord overlord) {
        this.overlord = overlord;
    }

    @Override
    public void removeOverlord(){
        overlord = null;
    }

    @Override
    public boolean hasOverlord() {
        return overlord != null;
    }
}
