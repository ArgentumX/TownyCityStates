package com.argemtum.townyCityStates;

import com.argemtum.townyCityStates.di.PluginModule;
import com.argemtum.townyCityStates.objects.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.IConfigRepository;
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
        ICityStateRepository csRepo = injector.getInstance(ICityStateRepository.class);
        csRepo.loadAll();

        IConfigRepository cRepo = injector.getInstance(IConfigRepository.class);
        cRepo.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Injector getInjector(){
        return injector;
    }
}
