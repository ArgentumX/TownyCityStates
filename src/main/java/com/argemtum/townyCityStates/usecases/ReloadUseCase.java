package com.argemtum.townyCityStates.usecases;

import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Inject;

public class ReloadUseCase {
    private IConfigRepository configRepository;
    private ICityStateRepository cityStateRepository;
    private ILocalizationRepository localizationRepositor;

    @Inject
    public ReloadUseCase(IConfigRepository configRepository, ICityStateRepository cityStateRepository, ILocalizationRepository localizationRepositor) {
        this.configRepository = configRepository;
        this.cityStateRepository = cityStateRepository;
        this.localizationRepositor = localizationRepositor;
    }

    public void ReloadSettings(){
        configRepository.load();
    }

    public void ReloadLocalization(){
        localizationRepositor.load(configRepository.GetInstance().getLocalization());
    }

    public void ReloadCities(){
        cityStateRepository.loadAll();
    }
}
