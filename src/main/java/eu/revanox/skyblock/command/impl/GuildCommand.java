package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.guild.model.SkyBlockGuild;
import eu.revanox.skyblock.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GuildCommand extends AbstractCommand {

    public GuildCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "guild", null, "clan", "gilde");
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 0) {
            player.sendMessage(ChatAction.of("/Gilde create <Name>"));
            player.sendMessage(ChatAction.of("/Gilde delete"));
            player.sendMessage(ChatAction.of("/Gilde leave"));
            return;
        }


        if(args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String guildName = args[1];

            if(SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt bereits eine Gilde!"));
                return;
            }

            if(SkyBlockPlugin.instance().getGuildManager().existsGuild(guildName) || !guildName.matches("[A-Za-z0-9-]+")) {
                player.sendMessage(ChatAction.failure("Dieser Name ist nicht vefügbar."));
                return;
            }

            SkyBlockPlugin.instance().getGuildManager().createGuild(player, guildName);
            player.sendMessage(ChatAction.of("Deine Gilde wurde erfolgreich erstellt."));
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("delete")) {

            if(!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if(SkyBlockPlugin.instance().getGuildManager().isMemberOfGuild(player) && !SkyBlockPlugin.instance().getGuildManager().isLeaderOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du bist kein Gildenbesitzer und kannst deine Gilde nur verlassen."));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            for (UUID guildMember : skyBlockGuild.getGuildMembers()) {
                Player guildPlayer = Bukkit.getPlayer(guildMember);

                if(guildPlayer == null) continue;
                if(!guildPlayer.isOnline()) continue;
                if(guildPlayer.getUniqueId().equals(player.getUniqueId())) continue;

                guildPlayer.sendMessage(ChatAction.failure("Deine Gilde wurde vom Gildenbesitzer gelöscht."));
            }

            SkyBlockPlugin.instance().getGuildManager().deleteGuild(player);
            player.sendMessage(ChatAction.of("Deine Gilde wurde erfolgreich gelöscht."));
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {

            if(!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if(SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du bist Gildenbesitzer und kannst deine Gilde nur löschen."));
                return;
            }

            if(!SkyBlockPlugin.instance().getGuildManager().isMemberOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du befindest dich in keiner Gilde!"));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);
            SkyBlockPlugin.instance().getGuildManager().kickPlayerFromGuild(player, skyBlockGuild);
            player.sendMessage(ChatAction.of("Du hast die Gilde erfolgreich verlassen."));

            for (UUID guildMember : skyBlockGuild.getGuildMembers()) {
                Player guildPlayer = Bukkit.getPlayer(guildMember);

                if(guildPlayer == null) continue;
                if(!guildPlayer.isOnline()) continue;

                guildPlayer.sendMessage(ChatAction.failure(player.getName() + " hat deine Gilde verlassen."));
            }
            return;
        }
    }
}
