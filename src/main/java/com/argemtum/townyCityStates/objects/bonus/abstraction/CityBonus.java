package com.argemtum.townyCityStates.objects.bonus.abstraction;

import com.argemtum.townyCityStates.listeners.abstraction.IHandler;
import com.argemtum.townyCityStates.objects.bonus.enums.EventSource;
import com.argemtum.townyCityStates.objects.bonus.enums.TrackType;
import org.bukkit.event.Event;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@ConfigSerializable
public abstract class CityBonus implements IHandler {
    private final transient List<EventHandler> handlers = new ArrayList<>();

    protected CityBonus() {
        registerHandlers();
    }

    protected abstract void registerHandlers();

    protected <T extends Event> void registerHandler(Class<T> eventClass, Consumer<T> handler, EventSource eventSource) {
        handlers.add(new EventHandler(eventClass, handler, eventSource));
    }

    public List<EventHandler> getEventHandlers() {
        return Collections.unmodifiableList(handlers);
    }
}