package com.argemtum.townyCityStates.commands;

import com.argemtum.townyCityStates.commands.abstraction.BaseCommand;
import com.argemtum.townyCityStates.commands.abstraction.CommandObject;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.objects.city.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.argemtum.townyCityStates.controllers.usecases.CityStateInfoUseCase;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TownyCityStatesCommand extends BaseCommand {
    private final Localization localization;
    private final CityStateInfoUseCase cityStateInfoUseCase;

    @Inject
    public TownyCityStatesCommand(
            ILocalizationRepository localizationRepository,
            CityStateInfoUseCase cityStateInfoUseCase
    ){
        this.localization = localizationRepository.GetInstance();
        this.cityStateInfoUseCase = cityStateInfoUseCase;
    }

    @Override
    protected boolean handleCommand(@NotNull CommandObject command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (!sender.hasPermission("tcs.user")) {
            sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
            return true;
        }

        if (!isPlayer(sender)) {
            // TODO only for players
            return true;
        }

        try {
            if (args.length > 0) {
                String arg0 = args[0].toLowerCase();
                return switch (arg0) {
                    case "list" -> parseList(sender, args);
                    default -> false;
                };
            }
            return false;
        }
        catch (Exception exception) {
            // TODO logger
            return true;
        }
    }

    private boolean parseList(CommandSender sender, String[] args) {
        List<CityState> cities = cityStateInfoUseCase.getCityStates();
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
        return true;
    }

    @Override
    protected @Nullable List<String> handleTabComplete(@NotNull CommandObject command) {
        String[] args = command.getArgs();

        if (args.length == 1) {
            return filterCompletions(args[0], Collections.singletonList("list"));
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