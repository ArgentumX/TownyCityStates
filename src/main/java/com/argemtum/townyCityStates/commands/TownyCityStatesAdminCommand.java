package com.argemtum.townyCityStates.commands;

import co.aikar.commands.annotation.*;
import co.aikar.commands.InvalidCommandArgument;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.controllers.services.CityStateService;
import com.argemtum.townyCityStates.controllers.usecases.CreateCityStateUseCase;
import com.argemtum.townyCityStates.controllers.usecases.ReloadUseCase;
import com.argemtum.townyCityStates.exceptions.absctraction.CityStatesException;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Inject;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("tcs_admin|tcsa")
@CommandPermission("tcs.admin")
public class TownyCityStatesAdminCommand extends BaseCommand {

    private final ReloadUseCase reloadUseCase;
    private final CreateCityStateUseCase createUseCase;
    private final Localization localization;
    private final CityStateService cityStateService;

    @Inject
    public TownyCityStatesAdminCommand(
            TownyCityStates plugin,
            ReloadUseCase reloadUseCase,
            CreateCityStateUseCase createUseCase,
            ILocalizationRepository localizationRepository,
            CityStateService cityStateService
    ) {
        super(plugin);
        this.reloadUseCase = reloadUseCase;
        this.createUseCase = createUseCase;
        this.localization = localizationRepository.GetInstance();
        this.cityStateService = cityStateService;
    }

    @Subcommand("reload settings")
    public void onReloadSettings(Player sender) {
        reloadUseCase.ReloadSettings();
        sender.sendMessage(localization.of(MessageNode.RELOAD_SETTINGS));
    }

    @Subcommand("reload cities")
    public void onReloadCities(Player sender) {
        int citiesAmount = reloadUseCase.ReloadCities();
        sender.sendMessage(localization.of(MessageNode.RELOAD_CITIES, citiesAmount));
    }

    @Subcommand("reload language")
    public void onReloadLanguage(Player sender) {
        String language = reloadUseCase.ReloadLocalization();
        sender.sendMessage(localization.of(MessageNode.RELOAD_LOCALIZATION, language));
    }

    @Subcommand("create")
    public void onCreate(Player sender, @Single String cityName) throws CityStatesException {
        createUseCase.createCityState(cityName);
        sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
    }

    @CommandPermission("tcs.admin")
    @Subcommand("create")
    public void onCreateWithRegion(Player sender, @Single String cityName, @Single String regionName) throws CityStatesException {
        createUseCase.createCityStateWithExistingRegion(sender, cityName, regionName);
        sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
    }

    @CommandPermission("tcs.admin")
    @Subcommand("create autoregion")
    public void onCreateWithAutoRegion(Player sender, @Single String cityName, int radius) throws CityStatesException {
        if (radius < 0) {
            sender.sendMessage(localization.of(MessageNode.MUST_BE_POSITIVE));
            return;
        }
        createUseCase.createCityStateWithAutoRegion(sender, cityName, radius);
        sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
    }

    @Completion("#city-states")
    @Subcommand("city set overlord")
    public void onCitySetOverlord(Player sender, @Single String cityName, @Single String townName) throws CityStatesException {
        CityState cityState = cityStateService.getCityState(cityName);
        if (cityState == null) {
            throw new InvalidCommandArgument("City state not found: " + cityName);
        }
        
        Town town = TownyAPI.getInstance().getTown(townName);
        if (town == null) {
            throw new InvalidCommandArgument("Town not found: " + townName);
        }
        
        cityStateService.SetCityStateOverlord(cityName, townName);
        sender.sendMessage(localization.of(MessageNode.SET_CITY_OVERLORD, cityName, townName));
    }

    @CompletionProvider
    public List<String> getCityStateCompletions(CommandCompletionContext c) {
        return cityStateService.getCityStates().stream()
                .map(CityState::getName)
                .collect(Collectors.toList());
    }

    @CatchUnknown
    public void onUnknown(Player sender) {
        sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
    }
}
