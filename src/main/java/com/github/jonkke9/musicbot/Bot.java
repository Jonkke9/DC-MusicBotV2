package com.github.jonkke9.musicbot;

import com.github.jonkke9.musicbot.audio.PlayerManager;
import net.dv8tion.jda.api.JDA;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bot {

    private JDA jda;
    private final Config config;
    private final ScheduledExecutorService executorService;
    private final PlayerManager playerManager;


    public Bot(final Config config) {
        this.config = config;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.playerManager = new PlayerManager(this);
    }


    public void setJda(final JDA jda) {
        this.jda = jda;
    }


    //Getters
    public JDA getJda() {
        return jda;
    }

    public Config getConfig() {
        return config;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public PlayerManager getPlayerManager () {
        return playerManager;
    }
}
