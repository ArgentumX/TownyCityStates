package com.argemtum.townyCityStates;

import com.argemtum.townyCityStates.di.PluginModule;
import com.argemtum.townyCityStates.objects.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TownyCityStates extends JavaPlugin {
    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));

        // loading data
        IConfigRepository cRepo = injector.getInstance(IConfigRepository.class);
        cRepo.load();

        ILocalizationRepository lRepo = injector.getInstance(ILocalizationRepository.class);
        lRepo.load(cRepo.GetInstance().getLocalization());

        ICityStateRepository csRepo = injector.getInstance(ICityStateRepository.class);
        csRepo.loadAll();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Injector getInjector(){
        return injector;
    }
}
