package com.argemtum.townyCityStates.listeners.abstraction;

import com.argemtum.townyCityStates.objects.bonus.abstraction.EventHandler;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IHandler {
    List<EventHandler> getEventHandlers();
}

