package com.argemtum.townyCityStates.objects.city.abstraction;

import com.argemtum.townyCityStates.objects.overlord.abstraction.Overlord;

import java.util.Optional;

public interface IOverlordable {
    Optional<Overlord> getOverlord();
    void setOverlord(Overlord overlord);
    void removeOverlord();
    boolean hasOverlord();
}
