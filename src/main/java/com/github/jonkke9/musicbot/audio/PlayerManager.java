package com.github.jonkke9.musicbot.audio;

import com.github.jonkke9.musicbot.Bot;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager extends DefaultAudioPlayerManager {
    private final Bot bot;

    private final Map<Long, GuildMusicManager> musicManagers;
    public PlayerManager(final Bot bot) {
        this.bot = bot;
        this.musicManagers = new HashMap<>();


        final YoutubeAudioSourceManager yt = new YoutubeAudioSourceManager(true);
        registerSourceManager(yt);
        AudioSourceManagers.registerLocalSource(this);
    }



    // Method to get the music manager for a guild
    public synchronized GuildMusicManager getMusicManager(final Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this, guild);
            guild.getAudioManager().setSendingHandler(guildMusicManager.sendHandler);
            return guildMusicManager;
        });
    }

    public void connectToVoiceChannel(final VoiceChannel channel) {
        final AudioManager audioManager = channel.getGuild().getAudioManager();
        getMusicManager(channel.getGuild()).scheduler.clearQueue();
        audioManager.openAudioConnection(channel);
    }

    public Collection<GuildMusicManager> getMusicManagers() {
        return  musicManagers.values();
    }
}
