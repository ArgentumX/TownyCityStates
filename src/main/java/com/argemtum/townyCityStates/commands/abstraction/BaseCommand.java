package com.argemtum.townyCityStates.commands.abstraction;

import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.exceptions.absctraction.CityStatesException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    protected static List<String> NumbersTabComplete = List.of(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    );

    protected void sendMessageIfPlayer(CommandSender sender, String message){
        if (!(sender instanceof Player player)){
            return;
        }
        player.sendMessage(message);
    }

    protected boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        CommandObject commandObject = new CommandObject(sender, command, label, args);
        return handleCommand(commandObject);
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        CommandObject commandObject = new CommandObject(sender, command, label, args);
        return handleTabComplete(commandObject);
    }

    protected abstract boolean handleCommand(CommandObject command);
    protected abstract @Nullable List<String> handleTabComplete(CommandObject command);

}
