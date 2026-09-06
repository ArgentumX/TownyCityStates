package com.argemtum.townyCityStates;

import co.aikar.commands.PaperCommandManager;
import com.argemtum.townyCityStates.commands.TownyCityStatesAdminCommand;
import com.argemtum.townyCityStates.commands.TownyCityStatesCommand;
import com.argemtum.townyCityStates.controllers.services.CityStateService;
import com.argemtum.townyCityStates.di.PluginModule;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public final class TownyCityStates extends JavaPlugin {
    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));

        IConfigRepository cRepo = injector.getInstance(IConfigRepository.class);
        cRepo.load();

        ILocalizationRepository lRepo = injector.getInstance(ILocalizationRepository.class);
        lRepo.load(cRepo.GetInstance().getLocalization());

        ICityStateRepository csRepo = injector.getInstance(ICityStateRepository.class);
        csRepo.loadAll();

        registerCommands();
    }

    private void registerCommands(){
        PaperCommandManager manager = new PaperCommandManager(this);

        CityStateService cityStateService = injector.getInstance(CityStateService.class);
        manager.getCommandCompletions().registerAsyncCompletion("city-states", context ->
                cityStateService.getCityStates().stream()
                        .map(CityState::getName)
                        .collect(Collectors.toList())
        );

        manager.registerCommand(injector.getInstance(TownyCityStatesAdminCommand.class));
        manager.registerCommand(injector.getInstance(TownyCityStatesCommand.class));
    }

    @Override
    public void onDisable() {
    }

    public Injector getInjector(){
        return injector;
    }
}
