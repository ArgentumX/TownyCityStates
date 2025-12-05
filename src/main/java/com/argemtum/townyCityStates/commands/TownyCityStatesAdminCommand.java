package com.argemtum.townyCityStates.commands;

import com.argemtum.townyCityStates.commands.abstraction.BaseCommand;
import com.argemtum.townyCityStates.commands.abstraction.CommandObject;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TownyCityStatesAdminCommand extends BaseCommand {

    private final ReloadUseCase reloadUseCase;
    private final CreateCityStateUseCase createUseCase;
    private final Localization localization;
    private final CityStateService cityStateService;

    @Inject
    public TownyCityStatesAdminCommand(
            ReloadUseCase reloadUseCase,
            CreateCityStateUseCase createUseCase,
            ILocalizationRepository localizationRepository,
            CityStateService cityStateService
    ) {
        this.reloadUseCase = reloadUseCase;
        this.createUseCase = createUseCase;
        this.localization = localizationRepository.GetInstance();
        this.cityStateService = cityStateService;
    }

    @Override
    protected boolean handleCommand(@NotNull CommandObject command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (!sender.hasPermission("tcs.admin") && !sender.isOp()) {
            sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
            return true;
        }

        try {
            if (args.length == 0) {
                return false;
            }

            String arg0 = args[0].toLowerCase();
            return switch (arg0) {
                case "reload" -> parseReload(sender, args);
                case "create" -> parseCreate(sender, args);
                case "city" -> parseCity(sender, args);
                default -> false;
            };
        } catch (CityStatesException exception) {
            sendMessageIfPlayer(sender, exception.getMessage());
            return true;
        }
    }

    @Override
    protected @Nullable List<String> handleTabComplete(@NotNull CommandObject command) {
        String[] args = command.getArgs();
        int length = args.length;

        if (length == 1) {
            return filterCompletions(args[0], List.of("create", "reload", "city"));
        } else if (length == 2) {
            return switch (args[0].toLowerCase()) {
                case "reload" -> filterCompletions(args[1], List.of("settings", "cities", "language"));
                case "city" -> filterCompletions(args[1], getCityStateNames());
                default -> Collections.emptyList();
            };
        } else if (length == 3) {
            return switch (args[0].toLowerCase()) {
                case "create" -> List.of("autoregion");
                case "city" -> List.of("set");
                default -> Collections.emptyList();
            };
        } else if (length == 4) {
            return switch (args[2].toLowerCase()) {
                case "autoregion" -> NumbersTabComplete;
                case "set" -> List.of("overlord");
                default -> Collections.emptyList();
            };
        } else if (length == 5 && "set".equals(args[2].toLowerCase()) && "overlord".equals(args[3].toLowerCase())) {
            return filterCompletions(args[4], getTownNames());
        }

        return Collections.emptyList();
    }

    private boolean parseReload(CommandSender sender, String[] args) {
        if (args.length != 2) return false;

        return switch (args[1].toLowerCase()) {
            case "settings" -> {
                reloadUseCase.ReloadSettings();
                sendMessageIfPlayer(sender, localization.of(MessageNode.RELOAD_SETTINGS));
                yield true;
            }
            case "cities" -> {
                int citiesAmount = reloadUseCase.ReloadCities();
                sendMessageIfPlayer(sender, localization.of(MessageNode.RELOAD_CITIES, citiesAmount));
                yield true;
            }
            case "language" -> {
                String language = reloadUseCase.ReloadLocalization();
                sendMessageIfPlayer(sender, localization.of(MessageNode.RELOAD_LOCALIZATION, language));
                yield true;
            }
            default -> false;
        };
    }

    private boolean parseCreate(CommandSender sender, String[] args) throws CityStatesException {
        if (args.length < 2) return false;
        String cityName = args[1];

        if (args.length == 2) {
            createUseCase.createCityState(cityName);
            sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(localization.of(MessageNode.NOT_PLAYER));
            return true;
        }

        if (args.length == 3) {
            String regionName = args[2];
            createUseCase.createCityStateWithExistingRegion(player, cityName, regionName);
            sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
            return true;
        } else if (args.length == 4 && "autoregion".equalsIgnoreCase(args[2])) {
            try {
                int radius = Integer.parseInt(args[3]);
                if (radius < 0) {
                    sender.sendMessage(localization.of(MessageNode.MUST_BE_POSITIVE));
                    return true;
                }
                createUseCase.createCityStateWithAutoRegion(player, cityName, radius);
                sender.sendMessage(localization.of(MessageNode.CITY_CREATED, cityName));
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(localization.of(MessageNode.MUST_BE_NUM));
                return true;
            }
        }
        return false;
    }

    private boolean parseCity(CommandSender sender, String[] args) throws CityStatesException {
        if (args.length < 3) return false;
        String subCommand = args[2].toLowerCase();

        return switch (subCommand) {
            case "set" -> parseCitySet(sender, args);
            default -> false;
        };
    }

    private boolean parseCitySet(CommandSender sender, String[] args) throws CityStatesException {
        if (args.length != 5) return false;
        String csName = args[1];
        String field = args[3].toLowerCase();
        String value = args[4];

        return switch (field) {
            case "overlord" -> {
                cityStateService.SetCityStateOverlord(csName, value);
                sender.sendMessage(localization.of(MessageNode.SET_CITY_OVERLORD, csName, value));
                yield true;
            }
            default -> false;
        };
    }

    private List<String> getCityStateNames() {
        return cityStateService.getCityStates().stream()
                .map(CityState::getName)
                .collect(Collectors.toList());
    }

    private List<String> getTownNames() {
        return TownyAPI.getInstance().getTowns().stream()
                .map(Town::getName)
                .collect(Collectors.toList());
    }

    private List<String> filterCompletions(String currentArg, List<String> completions) {
        if (currentArg.isEmpty()) return completions;

        String lowerArg = currentArg.toLowerCase();
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(lowerArg))
                .sorted()
                .collect(Collectors.toList());
    }
}