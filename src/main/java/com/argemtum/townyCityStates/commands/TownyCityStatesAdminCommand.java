package com.argemtum.townyCityStates.commands;

import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.argemtum.townyCityStates.usecases.ReloadUseCase;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TownyCityStatesAdminCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private ReloadUseCase reloadUseCase;
    private Localization localization;

    @Inject
    public TownyCityStatesAdminCommand(
        ReloadUseCase reloadUseCase,
        ILocalizationRepository localizationRepository
    ){
        this.reloadUseCase = reloadUseCase;
        localization = localizationRepository.GetInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("tcs.admin") && !sender.isOp()) {
            sender.sendMessage(localization.of(MessageNode.NO_PERMISSIONS));
            return true;
        }

        if (args.length > 0) {
            String arg0 = args[0].toLowerCase();
            switch (arg0) {
                case "reload":
                    return parseReload(sender, command, label, args);
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean parseReload(CommandSender sender, Command command, String label, String[] args){
        if (args.length == 2) {
            String arg1 = args[1].toLowerCase();
            return switch (arg1) {
                case "settings" -> {
                    reloadUseCase.ReloadSettings();
                    yield true;
                }
                case "cities" -> {
                    reloadUseCase.ReloadCities();
                    yield true;
                }
                case "language" -> {
                    reloadUseCase.ReloadLocalization();
                    yield true;
                }
                default -> false;
            };
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            return List.of("reload", "create");
        }
        else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    return List.of("settings", "cities", "language");
                default:
                    return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
