package com.argemtum.townyCityStates.objects.bonus.abstraction;

import com.argemtum.townyCityStates.objects.bonus.enums.EventSource;
import com.argemtum.townyCityStates.objects.bonus.enums.TrackType;
import org.bukkit.event.Event;

import java.util.function.Consumer;

public class EventHandler {
    private final Class<? extends Event> eventClass;
    private final Consumer<? extends Event> handler;
    private final EventSource eventSource;

    public EventHandler(Class<? extends Event> eventClass, Consumer<? extends Event> handler, EventSource eventSource) {
        this.eventClass = eventClass;
        this.handler = handler;
        this.eventSource = eventSource;
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> Consumer<T> getHandler() {
        return (Consumer<T>) handler;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}