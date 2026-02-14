package com.argemtum.townyCityStates.controllers.services;

import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.objects.overlord.TownOverlord;
import com.argemtum.townyCityStates.objects.overlord.abstraction.Overlord;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.google.inject.Inject;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import java.util.List;
import java.util.Objects;

public class CityStateService {
    private final ICityStateRepository cityStateRepository;

    @Inject
    public CityStateService(ICityStateRepository cityStateRepository){
        this.cityStateRepository = cityStateRepository;
    }

    public void SetCityStateOverlord(String cityStateName, String townName){
        CityState cs = cityStateRepository.getByName(cityStateName).orElseThrow();
        Town t = Objects.requireNonNull(TownyAPI.getInstance().getTown(townName));
        this.SetCityStateOverlord(cs, t);
    }

    public void SetCityStateOverlord(CityState cs, Town t){
        cs.getOverlordBehavior().setOverlord(new TownOverlord(t.getUUID()));
        cityStateRepository.save(cs);
    }

    public List<CityState> getCityStates(){
        return cityStateRepository.getAll().stream().toList();
    }

    public CityState getCityState(String name){
        return cityStateRepository.getByName(name).orElse(null);
    }
}
