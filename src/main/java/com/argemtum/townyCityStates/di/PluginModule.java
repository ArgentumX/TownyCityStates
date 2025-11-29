package com.argemtum.townyCityStates.di;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.usecases.CityStateRewardUseCase;
import com.argemtum.townyCityStates.usecases.CreateCityStateUseCase;
import com.google.inject.AbstractModule;

public class PluginModule extends AbstractModule {
    private final TownyCityStates plugin;

    public PluginModule(TownyCityStates plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(TownyCityStates.class).toInstance(plugin);
        bind(CityStateRewardUseCase.class).asEagerSingleton();
        bind(CreateCityStateUseCase.class).asEagerSingleton();
    }
}
