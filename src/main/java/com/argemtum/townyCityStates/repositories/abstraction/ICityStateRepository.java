package com.argemtum.townyCityStates.repositories.abstraction;

import com.argemtum.townyCityStates.objects.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.abstraction.IIdentifiableRepository;

import java.util.Optional;
import java.util.UUID;

public interface ICityStateRepository extends IIdentifiableRepository<CityState, UUID> {
    Optional<CityState> getByName(String name);
}
