package com.github.jonkke9.musicbot.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.Set;

/**
 * Represents a command that can be executed in the Discord bot.
 */
public interface Command {

    /**
     * Gets the name of the command.
     *
     * @return the name of the command.
     */
    String getName();

    /**
     * Gets a set of aliases for the command.
     *
     * @return a set of aliases for the command.
     */
    Set<String> getAliases();

    /**
     * Gets the description of the command.
     *
     * @return the description of the command.
     */
    String getDescription();

    boolean canRun();

    String getUsage();

    /**
     * Executes the command with the given message and arguments.
     *
     * @param cmd the message that triggered the command.
     * @param args the arguments passed to the command.
     */
    void execute(Message cmd, String ...args);
}
