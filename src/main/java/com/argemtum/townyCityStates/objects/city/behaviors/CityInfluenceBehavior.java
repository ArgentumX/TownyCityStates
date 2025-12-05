package com.argemtum.townyCityStates.objects.city.behaviors;

import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.objects.city.abstraction.IInfluenceable;
import com.argemtum.townyCityStates.objects.city.behaviors.abstraction.CityBehavior;
import com.argemtum.townyCityStates.objects.influencer.abstraction.Influencer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConfigSerializable
public class CityInfluenceBehavior extends CityBehavior implements IInfluenceable {
    @Setting private Map<UUID, Influencer> influencers;
    @Setting private UUID overlordId;

    @Nullable
    private transient Influencer overlord = null;

    private CityInfluenceBehavior() { }

    public CityInfluenceBehavior(CityState cityState) {
        this(cityState, null);
    }

    public CityInfluenceBehavior(CityState cityState, Collection<Influencer> influencers) {
        init(cityState);
        this.influencers = (influencers == null)
                ? new HashMap<>()
                : influencers.stream()
                .collect(Collectors.toMap(Influencer::getInfluencerId, Function.identity()));
    }
    @Override
    public void init(CityState cityState){
        super.init(cityState);
        if (overlordId == null) { return; }
        getInfluencer(overlordId).ifPresent((influencer) -> overlord = influencer);
    }
    @Override
    public void addInfluencer(@NotNull Influencer influencer) {
        influencers.put(influencer.getInfluencerId(), influencer);
    }

    @Override
    public void removeInfluencer(UUID id) {
        influencers.remove(id);
    }

    @Override
    public void clearInfluencers() {
        influencers.clear();
    }

    @Override
    public Optional<Influencer> getInfluencer(UUID id) {
        return Optional.ofNullable(influencers.get(id));
    }

    public boolean hasInfluencer(UUID id) {
        return influencers.containsKey(id);
    }

    @Override
    public Collection<Influencer> getInfluencers() {
        return influencers.values();
    }
}