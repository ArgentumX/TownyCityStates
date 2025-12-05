package com.argemtum.townyCityStates.controllers.usecases;

import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.google.inject.Inject;

import java.util.List;

public class CityStateInfoUseCase {
    private final ICityStateRepository cityStateRepository;
    @Inject
    public CityStateInfoUseCase(ICityStateRepository cityStateRepository){
        this.cityStateRepository = cityStateRepository;
    }

    public List<CityState> getCityStates() {
        return cityStateRepository.getAll().stream().toList();
    }
}
