package com.argemtum.townyCityStates.di;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.factories.ConfigFactory;
import com.argemtum.townyCityStates.factories.abstraction.IConfigFactory;
import com.argemtum.townyCityStates.regions.WorldGuardAdapter;
import com.argemtum.townyCityStates.regions.abstraction.IRegionAdapter;
import com.argemtum.townyCityStates.repositories.CityStateYamlRepository;
import com.argemtum.townyCityStates.repositories.ConfigYamlRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.towny.TownyAdapter;
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

        // Factories
        bind(IConfigFactory.class).to(ConfigFactory.class);

        // Repositories
        bind(IConfigRepository.class)  // Новый биндинг
                .to(ConfigYamlRepository.class)
                .asEagerSingleton();
        bind(ICityStateRepository.class)
                .to(CityStateYamlRepository.class)
                .asEagerSingleton();

        // UseCases
        bind(CityStateRewardUseCase.class).asEagerSingleton();
        bind(CreateCityStateUseCase.class).asEagerSingleton();

        // External APIs
        bind(IRegionAdapter.class)
                .to(WorldGuardAdapter.class)
                .asEagerSingleton();
        bind(TownyAdapter.class)
                .asEagerSingleton();
    }
}
