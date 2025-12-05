package com.argemtum.townyCityStates.objects.city.abstraction;

import com.argemtum.townyCityStates.objects.influencer.abstraction.Influencer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IInfluenceable {
    void addInfluencer(Influencer influencer);
    void removeInfluencer(UUID id);
    void clearInfluencers();
    Optional<Influencer> getInfluencer(UUID id);
    boolean hasInfluencer(UUID id);
    Collection<Influencer> getInfluencers();
}