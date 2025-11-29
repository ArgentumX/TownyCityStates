package com.argemtum.townyCityStates;

import com.argemtum.townyCityStates.di.PluginModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public final class TownyCityStates extends JavaPlugin {
    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new PluginModule(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Injector GetInjector(){
        return injector;
    }
}
