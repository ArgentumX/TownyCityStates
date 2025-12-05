package com.argemtum.townyCityStates.objects.bonus;

import com.argemtum.townyCityStates.objects.bonus.abstraction.CityBonus;
import com.argemtum.townyCityStates.objects.bonus.enums.EventSource;
import com.argemtum.townyCityStates.objects.bonus.enums.TrackType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class HealthBonus extends CityBonus {
    @Setting
    private int bonusHealth;

    public HealthBonus(int bonusHealth) {
        super();
        this.bonusHealth = bonusHealth;
    }

    @Override
    protected void registerHandlers() {
        registerHandler(PlayerJoinEvent.class, this::onPlayerJoin, EventSource.PLAYER);
        registerHandler(PlayerQuitEvent.class, this::onPlayerQuit, EventSource.PLAYER);
    }

    private void onPlayerJoin(PlayerJoinEvent event) {
        double hpBoost = bonusHealth;
        event.getPlayer().setMaxHealth(event.getPlayer().getMaxHealth() + hpBoost);
        event.getPlayer().setHealth(event.getPlayer().getHealth() + hpBoost);
    }

    private void onPlayerQuit(PlayerQuitEvent event) {
        double hpBoost = bonusHealth;
        event.getPlayer().setMaxHealth(event.getPlayer().getMaxHealth() - hpBoost);
        event.getPlayer().setHealth(Math.min(event.getPlayer().getHealth(), event.getPlayer().getMaxHealth()));
    }
}