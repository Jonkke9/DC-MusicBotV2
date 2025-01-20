package com.github.jonkke9.musicbot.commands;

import com.github.jonkke9.musicbot.Bot;
import com.github.jonkke9.musicbot.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler  extends ListenerAdapter {

    public final static Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

    private final Map<String, Command> commands;
    private final Bot bot;

    public CommandHandler (final Bot bot) {
        this.commands = new HashMap<>();
        this.bot = bot;
    }

    public void registerCommand(final Command command) {
        commands.put(command.getName(), command);
        for (final String alias : command.getAliases()) {
            commands.put(alias, command);
        }
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        bot.getExecutorService().execute(() -> {
            handleMessage(event);
        });
    }

    private void handleMessage(final MessageReceivedEvent event) {
        final String msg = event.getMessage().getContentRaw();

        if (!msg.startsWith(bot.getConfig().get("bot.prefix"))) {
            return;
        }

        final String[] parts = msg.split(" ");
        final Command cmd = commands.get(parts[0].substring(bot.getConfig().get("bot.prefix").length()));


        if (cmd == null) {
            return;
        }

        final String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);

        LOGGER.debug("Executing command: " + cmd.getName());
        cmd.execute(event.getMessage(), args);
    }
}
