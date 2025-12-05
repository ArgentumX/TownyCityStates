package com.argemtum.townyCityStates.listeners;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.objects.bonus.abstraction.CityBonus;
import com.argemtum.townyCityStates.objects.bonus.abstraction.EventHandler;
import com.argemtum.townyCityStates.objects.bonus.enums.EventSource;
import com.argemtum.townyCityStates.objects.bonus.enums.TrackType;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.objects.city.abstraction.IBonusProvider;
import com.argemtum.townyCityStates.objects.overlord.abstraction.Overlord;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.google.inject.Inject;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.Event;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class BonusRegistry {
    private final Map<UUID, Map<Class<? extends Event>, List<Consumer<? extends Event>>>> handlerMap = new HashMap<>();
    private final ICityStateRepository cityStatesRepository;
    private final Logger logger;

    @Inject
    public BonusRegistry(ICityStateRepository cityStatesRepository, TownyCityStates plugin) {
        this.cityStatesRepository = cityStatesRepository;
        this.logger = plugin.getLogger();
    }

    public void registerAllHandlers() {
        handlerMap.clear();
        Collection<CityState> allCities = cityStatesRepository.getAll();
        for (CityState city : allCities) {
            IBonusProvider bonusProvider = city.getBonusBehavior();
            Optional<Overlord> overlord = city.getOverlordBehavior().getOverlord();

            if (overlord.isEmpty()){ continue; }

            // Towns
            List<Town> relatedTowns = overlord.get().getRelatedTowns();
            for (Town t : relatedTowns) {
                for (CityBonus bonus : bonusProvider.getAllBonuses()) {
                    registerHandlersForUUID(t.getUUID(), bonus, EventSource.TOWN);
                }
            }

            // Players
            List<Resident> relatedPlayers = overlord.get().getRelatedPlayers();
            for (Resident r : relatedPlayers) {
                for (CityBonus bonus : bonusProvider.getAllBonuses()) {
                    registerHandlersForUUID(r.getUUID(), bonus, EventSource.PLAYER);
                }
            }
        }
        logger.info("Registered handlers for " + handlerMap.size() + " entities.");
    }

    private void registerHandlersForUUID(UUID uuid, CityBonus bonus, EventSource targetSource) {
        List<EventHandler> bonusHandlers = bonus
                .getEventHandlers()
                .stream()
                .filter(b -> b.getEventSource() == targetSource)
                .toList();

        for (EventHandler eventHandler : bonusHandlers) {
            Consumer<? extends Event> handler = eventHandler.getHandler();
            Class<? extends Event> eventClass = eventHandler.getEventClass();
            handlerMap
                    .computeIfAbsent(uuid, k -> new HashMap<>())
                    .computeIfAbsent(eventClass, k -> new ArrayList<>())
                    .add(handler);

            logger.fine("Registered handler for event " + eventClass.getSimpleName() + " on UUID " + uuid + " from bonus " + bonus.getClass().getSimpleName());
        }
    }

    public void unregisterHandlersForUUID(UUID uuid) {
        handlerMap.remove(uuid);
        logger.info("Unregistered handlers for UUID " + uuid);
    }

    public void handleEvent(Event event, UUID uuid) {
        Map<Class<? extends Event>, List<Consumer<? extends Event>>> entityHandlers = handlerMap.get(uuid);
        if (entityHandlers == null) return;

        Class<? extends Event> eventClass = event.getClass();
        List<Consumer<? extends Event>> handlers = entityHandlers.get(eventClass);
        if (handlers == null || handlers.isEmpty()) return;

        for (Consumer<? extends Event> handler : handlers) {
            try {
                @SuppressWarnings("unchecked")
                Consumer<Event> castedHandler = (Consumer<Event>) handler;
                castedHandler.accept(event);
            } catch (Exception e) {
                logger.warning("Error handling event " + eventClass.getSimpleName() + " for UUID " + uuid + ": " + e.getMessage());
            }
        }
    }
}