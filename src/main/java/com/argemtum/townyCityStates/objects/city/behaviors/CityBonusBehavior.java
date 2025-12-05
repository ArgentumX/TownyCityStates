package com.argemtum.townyCityStates.objects.city.behaviors;

import com.argemtum.townyCityStates.objects.bonus.abstraction.CityBonus;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.objects.city.abstraction.IBonusProvider;
import com.argemtum.townyCityStates.objects.city.behaviors.abstraction.CityBehavior;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConfigSerializable
public class CityBonusBehavior extends CityBehavior implements IBonusProvider {

    @Setting
    private final Map<String, CityBonus> bonuses = new ConcurrentHashMap<>();

    private CityBonusBehavior() { }

    public CityBonusBehavior(CityState cityState) {
        init(cityState);
    }

    @Override
    public <T extends CityBonus> void addBonus(T bonus) {
        if (bonus == null) {
            throw new IllegalArgumentException("Bonus cannot be null");
        }
        bonuses.put(bonus.getClass().getName(), bonus);
    }

    @Override
    public <T extends CityBonus> void removeBonus(Class<T> bonusType) {
        if (bonusType == null) return;
        bonuses.remove(bonusType.getName());
    }

    @Override
    public void clearBonuses() {
        bonuses.clear();
    }

    @Override
    public <T extends CityBonus> Optional<T> getBonus(Class<T> bonusType) {
        if (bonusType == null) return Optional.empty();
        CityBonus bonus = bonuses.get(bonusType.getName());
        if (bonus == null) return Optional.empty();
        return Optional.of(bonusType.cast(bonus));
    }

    @Override
    public <T extends CityBonus> boolean hasBonus(Class<T> bonusType) {
        if (bonusType == null) return false;
        return bonuses.containsKey(bonusType.getName());
    }

    @Override
    public Collection<CityBonus> getAllBonuses() {
        return Collections.unmodifiableCollection(bonuses.values());
    }
}