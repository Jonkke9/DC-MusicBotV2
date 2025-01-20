package com.github.jonkke9.musicbot.commands;

import com.github.jonkke9.musicbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public abstract class MusicPlayerCommand implements Command {

    private final Bot bot;

    public MusicPlayerCommand(final Bot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(final Message cmd, final String... args) {

        final Guild guild = cmd.getGuild();
        final Member selfmember = guild.getSelfMember();
        final GuildVoiceState selfVoiceState = selfmember.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            cmd.reply(bot.getConfig().get("message.bot-not-in-vc")).queue();
            return;
        }
        final Member member = cmd.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            cmd.reply(bot.getConfig().get("message.user-not-in-vc")).queue();
            return;
        }

        final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel().asVoiceChannel();
        final VoiceChannel selfVoiceChannel = selfVoiceState.getChannel().asVoiceChannel();

        if (!memberVoiceChannel.equals(selfVoiceChannel)) {
            cmd.reply(bot.getConfig().get("message.not-in-same-vc")).queue();
            return;
        }

        executeMusicCommand(cmd, args);
    }

    abstract void executeMusicCommand(final Message cmd, final String... args);
}
