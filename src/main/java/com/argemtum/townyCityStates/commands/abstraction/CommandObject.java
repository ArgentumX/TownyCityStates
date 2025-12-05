package com.argemtum.townyCityStates.commands.abstraction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandObject {
    @NotNull CommandSender sender;
    @NotNull Command command;
    @NotNull String label;
    @NotNull String @NotNull [] args;

    public CommandObject(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args){
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public String @NotNull [] getArgs() {
        return args;
    }

    public @NotNull String getLabel() {
        return label;
    }

    public @NotNull Command getCommand() {
        return command;
    }

    public @NotNull CommandSender getSender() {
        return sender;
    }
}