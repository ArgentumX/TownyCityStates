package com.argemtum.townyCityStates.objects.influencer.abstraction;

import org.checkerframework.checker.units.qual.C;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.UUID;

@ConfigSerializable
public abstract class Influencer {
    @Setting
    protected float influence;

    @Setting
    protected UUID influencerId;

    protected Influencer(){}

    public Influencer(UUID influencerId, float initialInfluence) {
        this.influencerId = influencerId;
        this.influence = initialInfluence;
    }

    public UUID getInfluencerId() {
        return influencerId;
    }

    public float getInfluence() {
        return influence;
    }

    public void addInfluence(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive for addInfluence");
        }
        this.influence += amount;
    }

    public void reduceInfluence(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive for reduceInfluence");
        }
        this.influence = Math.max(0, this.influence - amount);
    }
}