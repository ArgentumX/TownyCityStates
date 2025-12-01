package com.argemtum.townyCityStates.commands.abstraction;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand {

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
}
