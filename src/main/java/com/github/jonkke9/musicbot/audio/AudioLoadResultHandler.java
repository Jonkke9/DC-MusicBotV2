package com.github.jonkke9.musicbot.audio;

import com.github.jonkke9.musicbot.Bot;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// This class handles the results of loading audio tracks or playlists.
public final class AudioLoadResultHandler implements com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler {

    public final static Logger LOGGER = LoggerFactory.getLogger(AudioLoadResultHandler.class);
    private final GuildMusicManager musicManager;
    private final Message msg;
    private final Bot bot;

    public AudioLoadResultHandler(final Message msg, final GuildMusicManager musicManager, final Bot bot) {
        this.msg = msg;
        this.musicManager = musicManager;
        this.bot = bot;
    }

    // This method is called when a single track is loaded.
    @Override
    public void trackLoaded(final AudioTrack track) {
        // If a track is already playing, notify user that track has been added to the queue
        reply(bot.getConfig().get("message.added-one-to-queue", track.getInfo().title, String.valueOf(musicManager.scheduler.getQueue().size() + 1)));

        musicManager.scheduler.queue(track); // Add track to the queue
    }

    // This method is called when a playlist is loaded (multiple tracks)
    @Override
    public void playlistLoaded(final @NotNull AudioPlaylist audioPlaylist) {
        final List<AudioTrack> tracks = audioPlaylist.getTracks();

        // This meas that playlist is actually a YouTube search result. We are only going to take the first track and ignore the rest
        if (audioPlaylist.isSearchResult()) {
            // If a track is already playing, notify user that track has been added to the queue
            reply(bot.getConfig().get("message.added-one-to-queue", tracks.get(0).getInfo().title, String.valueOf(musicManager.scheduler.getQueue().size() + 1)));
            musicManager.scheduler.queue(tracks.get(0)); // Add track to the queue
        } else {
            // If the playlist is not a search result, queue all tracks
            for (final AudioTrack track : tracks) {
                musicManager.scheduler.queue(track);
            }
            // Tell the user how many tracks were added to the queue
            reply(bot.getConfig().get("message.added-many-to-queue", String.valueOf(tracks.size())));
        }
    }

    // This method is called when no matches are found for the requested track or playlist.
    @Override
    public void noMatches() {
        reply(bot.getConfig().get("message.no-matches"));
        // The bot joins voice channel when play command is used. if loading fails we want to make sure that the bot is not left to the channel
        if (musicManager.player.getPlayingTrack() == null) {
            msg.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    // This method is called when loading a track or playlist fails.
    @Override
    public void loadFailed(final FriendlyException e) {
        LOGGER.warn("Track loading failed.", e);
        e.printStackTrace();
        reply(bot.getConfig().get("message.no-matches"));
        // The bot joins voice channel when play command is used. if loading fails we want to make sure that the bot is not left to the channel
        if (musicManager.player.getPlayingTrack() == null) {
            msg.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    private void reply(final String reply) {
        msg.reply(reply).queue();
    }
}
