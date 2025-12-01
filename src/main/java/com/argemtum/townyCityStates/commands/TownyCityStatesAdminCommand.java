package com.argemtum.townyCityStates.commands;

import com.argemtum.townyCityStates.commands.abstraction.BaseCommand;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.exceptions.absctraction.CityStatesException;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.argemtum.townyCityStates.usecases.CreateCityStateUseCase;
import com.argemtum.townyCityStates.usecases.ReloadUseCase;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TownyCityStatesAdminCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private ReloadUseCase reloadUseCase;
    private CreateCityStateUseCase createUseCase;
    private Localization localization;

    @Inject
    public TownyCityStatesAdminCommand(
        ReloadUseCase reloadUseCase,
        CreateCityStateUseCase createUseCase,
        ILocalizationRepository localizationRepository
    ){
        this.reloadUseCase = reloadUseCase;
        this.createUseCase = createUseCase;
        this.localization = localizationRepository.GetInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("tcs.admin") && !sender.isOp()) {
            sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
            return true;
        }
        try {
            if (args.length > 0) {
                String arg0 = args[0].toLowerCase();
                switch (arg0) {
                    case "reload":
                        return parseReload(sender, command, label, args);
                    case "create":
                        return parseCreate(sender, command, label, args);
                    default:
                        return false;
                }
            }
            return false;
        }
        catch (CityStatesException exception){
            sendMessageIfPlayer(sender, exception.getMessage());
            return true;
        }
    }

    private boolean parseReload(CommandSender sender, Command command, String label, String[] args){
        if (args.length == 2) {
            String arg1 = args[1].toLowerCase();
            return switch (arg1) {
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
        return false;
    }

    private boolean parseCreate(CommandSender sender, Command command, String label, String[] args) throws CityStatesException {
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
        }
        else if (args.length == 4 && args[2].equalsIgnoreCase("autoregion")) {
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
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        int length = args.length;
        if (length == 1) {
            return filterCompletions(args[0], Arrays.asList("create", "reload"));
        }
        else if (length == 2) {
            switch (args[0].toLowerCase()) {
                case "create":
                    return Collections.emptyList(); // Имя города не предлагаем
                case "reload":
                    return filterCompletions(args[1], Arrays.asList("settings", "cities", "language"));
                default:
                    return Collections.emptyList();
            }
        }
        if (length == 3 && args[0].equalsIgnoreCase("create")) {
            return filterCompletions(args[2], List.of("autoregion"));
        }

        if (length == 4 && args[0].equalsIgnoreCase("create") && args[2].equalsIgnoreCase("autoregion")) {
            return NumbersTabComplete;
        }

        return Collections.emptyList();
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
