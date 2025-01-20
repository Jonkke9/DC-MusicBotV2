package com.github.jonkke9.musicbot.commands;

import com.github.jonkke9.musicbot.Bot;
import com.github.jonkke9.musicbot.audio.AudioLoadResultHandler;
import com.github.jonkke9.musicbot.audio.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class PlayCmd implements Command{

    private final Bot bot;

    public PlayCmd(final Bot bot) {
        this.bot = bot;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getName() {
        return "play";
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("p");
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getDescription() {
        return "";
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getUsage() {
        return "";
    }

    @Override
    public void execute(final Message cmd, final String @NotNull ... args) {
        if (args.length == 0 || args[0].length() == 0) {
            cmd.reply(this.getUsage()).queue();
            return;
        }

        final GuildVoiceState memberVoiceState = cmd.getMember().getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            cmd.reply( bot.getConfig().get("message.user-not-in-vc")).queue();
            return;
        }

        final Guild guild = cmd.getGuild();
        final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel().asVoiceChannel();
        final GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();

        if (selfVoiceState.inAudioChannel()) {
            final VoiceChannel selfVoiceChannel = selfVoiceState.getChannel().asVoiceChannel();
            if (!memberVoiceChannel.equals(selfVoiceChannel)) {
                cmd.reply( bot.getConfig().get("message.not-in-same-vc")).queue();
                return;
            }
        } else {
            bot.getPlayerManager().connectToVoiceChannel(memberVoiceChannel);
        }
        final GuildMusicManager musicManager = bot.getPlayerManager().getMusicManager(guild);

        bot.getPlayerManager().loadItemOrdered(musicManager, args[0], new AudioLoadResultHandler(cmd, musicManager, bot));
    }
}
