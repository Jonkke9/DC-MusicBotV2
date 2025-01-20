package com.github.jonkke9.musicbot;

import com.github.jonkke9.musicbot.commands.CommandHandler;
import com.github.jonkke9.musicbot.commands.PlayCmd;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        JDALogger.setFallbackLoggerEnabled(false);
        final Config config = Config.getInstance();
        final Bot bot = new Bot(config);

        final JDABuilder builder = JDABuilder.createDefault(System.getenv("BOT_TOKEN"));
        final CommandHandler commandHandler = new CommandHandler(bot);

        //register commands
        commandHandler.registerCommand(new PlayCmd(bot));

        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(commandHandler);
        bot.setJda(builder.build());

    }
}