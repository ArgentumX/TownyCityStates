package com.argemtum.townyCityStates.commands;

import co.aikar.commands.annotation.*;
import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.argemtum.townyCityStates.controllers.usecases.CityStateInfoUseCase;
import com.google.inject.Inject;
import org.bukkit.entity.Player;

@CommandAlias("tcs|townycitystates")
@CommandPermission("tcs.user")
public class TownyCityStatesCommand extends BaseCommand {
    private final Localization localization;
    private final CityStateInfoUseCase cityStateInfoUseCase;

    @Inject
    public TownyCityStatesCommand(
            TownyCityStates plugin,
            ILocalizationRepository localizationRepository,
            CityStateInfoUseCase cityStateInfoUseCase
    ){
        super(plugin);
        this.localization = localizationRepository.GetInstance();
        this.cityStateInfoUseCase = cityStateInfoUseCase;
    }

    @Default
    public void onDefault(Player sender) {
        sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
    }

    @Subcommand("list")
    public void onList(Player sender) {
        var cities = cityStateInfoUseCase.getCityStates();
        String header = localization.of(MessageNode.CITY_LIST_HEADER);
        sender.sendMessage(header);
        if (cities.isEmpty()) {
            sender.sendMessage(localization.of(MessageNode.CITY_LIST_EMPTY));
        } else {
            for (CityState city : cities) {
                String cityName = city.getName();
                String line = localization.of(MessageNode.CITY_LIST_ENTRY, cityName);
                sender.sendMessage(line);
            }
        }
    }
}
