package com.argemtum.townyCityStates.di;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.commands.TownyCityStatesAdminCommand;
import com.argemtum.townyCityStates.commands.TownyCityStatesCommand;
import com.argemtum.townyCityStates.factories.ConfigFactory;
import com.argemtum.townyCityStates.factories.abstraction.IConfigFactory;
import com.argemtum.townyCityStates.adapters.WorldGuardAdapter;
import com.argemtum.townyCityStates.listeners.BonusRegistry;
import com.argemtum.townyCityStates.repositories.CityStateYamlRepository;
import com.argemtum.townyCityStates.repositories.ConfigYamlRepository;
import com.argemtum.townyCityStates.repositories.LocalizationRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.adapters.TownyAdapter;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.argemtum.townyCityStates.controllers.usecases.CityStateInfoUseCase;
import com.argemtum.townyCityStates.controllers.usecases.CityStateRewardUseCase;
import com.argemtum.townyCityStates.controllers.usecases.CreateCityStateUseCase;
import com.argemtum.townyCityStates.controllers.usecases.ReloadUseCase;
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
        bind(ILocalizationRepository.class)
                .to(LocalizationRepository.class)
                .asEagerSingleton();

        // UseCases
        bind(CityStateRewardUseCase.class);
        bind(CreateCityStateUseCase.class);
        bind(ReloadUseCase.class);
        bind(CityStateInfoUseCase.class);

        // External APIs
        bind(WorldGuardAdapter.class)
                .asEagerSingleton();
        bind(TownyAdapter.class)
                .asEagerSingleton();

        // Commands
        bind(TownyCityStatesAdminCommand.class);
        bind(TownyCityStatesCommand.class);

        // Listeners
        bind(BonusRegistry.class).asEagerSingleton();
    }
}
