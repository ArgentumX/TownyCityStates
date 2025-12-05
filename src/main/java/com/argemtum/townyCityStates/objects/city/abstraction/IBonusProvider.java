package com.argemtum.townyCityStates.objects.city.abstraction;

import com.argemtum.townyCityStates.objects.bonus.abstraction.CityBonus;

import java.util.Collection;
import java.util.Optional;

public interface IBonusProvider {
    <T extends CityBonus> void addBonus(T bonus);
    <T extends CityBonus> void removeBonus(Class<T> bonusType);
    void clearBonuses();
    <T extends CityBonus> Optional<T> getBonus(Class<T> bonusType);
    <T extends CityBonus> boolean hasBonus(Class<T> bonusType);
    Collection<CityBonus> getAllBonuses();
}