package com.argemtum.townyCityStates;

import com.argemtum.townyCityStates.commands.TownyCityStatesAdminCommand;
import com.argemtum.townyCityStates.commands.TownyCityStatesCommand;
import com.argemtum.townyCityStates.di.PluginModule;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

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
        TownyCityStatesAdminCommand tcsAdminCommand = injector.getInstance(TownyCityStatesAdminCommand.class);
        tcsAdminCommand.register();

        TownyCityStatesCommand tcsCommand = injector.getInstance(TownyCityStatesCommand.class);
        tcsCommand.register();
    }

    @Override
    public void onDisable() {
    }

    public Injector getInjector(){
        return injector;
    }
}
