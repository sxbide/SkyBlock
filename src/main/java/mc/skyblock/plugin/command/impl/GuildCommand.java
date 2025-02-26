package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.guild.model.SkyBlockGuild;
import mc.skyblock.plugin.util.ChatAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildCommand extends AbstractCommand {

    private Map<Player, SkyBlockGuild> guildInviteMap = new HashMap<>();

    public GuildCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "guild", null, "clan", "gilde");
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 0) {
            player.sendMessage(ChatAction.of("/Gilde create <Name>"));
            player.sendMessage(ChatAction.of("/Gilde invite <Spielername>"));
            player.sendMessage(ChatAction.of("/Gilde kick <Spielername>"));
            player.sendMessage(ChatAction.of("/Gilde accept"));
            player.sendMessage(ChatAction.of("/Gilde deny"));
            player.sendMessage(ChatAction.of("/Gilde delete"));
            player.sendMessage(ChatAction.of("/Gilde info"));
            player.sendMessage(ChatAction.of("/Gilde leave"));
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("info")) {

            if (!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            player.sendMessage(ChatAction.of("Gilden-Information " + skyBlockGuild.getGuildName()));
            player.sendMessage(ChatAction.of("Erstellt am: " + new SimpleDateFormat("dd.MM.yyyy").format(skyBlockGuild.getCreatedAt())));
            player.sendMessage(ChatAction.of("Mitglieder (" + skyBlockGuild.getGuildMembers().size() + skyBlockGuild.getMaxMembers() + "):"));

            for (UUID guildMember : skyBlockGuild.getGuildMembers()) {
                player.sendMessage(ChatAction.of(Bukkit.getOfflinePlayer(guildMember).getName() +
                        (skyBlockGuild.getLeaderUniqueId().equals(guildMember) ? "(Gründer): " : ": ") + (Bukkit.getOfflinePlayer(guildMember).isOnline() ? "§aOnline" : "§cOffline")));
            }

            return;
        }


        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String guildName = args[1];

            if (SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt bereits eine Gilde!"));
                return;
            }

            if (SkyBlockPlugin.instance().getGuildManager().existsGuild(guildName) || !guildName.matches("[A-Za-z0-9-]+") || guildName.length() > 10 || guildName.length() < 2) {
                player.sendMessage(ChatAction.failure("Dieser Name ist nicht vefügbar."));
                return;
            }

            SkyBlockPlugin.instance().getGuildManager().createGuild(player, guildName);
            player.sendMessage(ChatAction.of("Deine Gilde wurde erfolgreich erstellt."));
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("delete")) {

            if (!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if (SkyBlockPlugin.instance().getGuildManager().isMemberOfGuild(player) && !SkyBlockPlugin.instance().getGuildManager().isLeaderOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du bist kein Gildenbesitzer und kannst deine Gilde nur verlassen."));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            for (UUID guildMember : skyBlockGuild.getGuildMembers()) {
                Player guildPlayer = Bukkit.getPlayer(guildMember);

                if (guildPlayer == null) continue;
                if (!guildPlayer.isOnline()) continue;
                if (guildPlayer.getUniqueId().equals(player.getUniqueId())) continue;

                guildPlayer.sendMessage(ChatAction.failure("Deine Gilde wurde vom Gildenbesitzer gelöscht."));
            }

            SkyBlockPlugin.instance().getGuildManager().deleteGuild(player);
            player.sendMessage(ChatAction.of("Deine Gilde wurde erfolgreich gelöscht."));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {

            // /gilde invite-0 player-1
            Player targetPlayer = Bukkit.getPlayer(args[1]);

            if (!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if (!SkyBlockPlugin.instance().getGuildManager().isLeaderOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du kannst als Gildenmitglied keine Spieler einladen."));
                return;
            }

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(ChatAction.failure("Du kannst diesen Spieler keine Gildeneinladung senden."));
                return;
            }

            if (SkyBlockPlugin.instance().getGuildManager().isInGuild(targetPlayer)) {
                player.sendMessage(ChatAction.failure("Dieser Spieler ist bereits in einer Gilde."));
                return;
            }


            if (this.guildInviteMap.containsKey(targetPlayer)) {
                player.sendMessage(ChatAction.failure("Dieser Spieler hat bereits eine offene Gildeneinladung."));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);
            this.guildInviteMap.put(targetPlayer, skyBlockGuild);
            player.sendMessage(ChatAction.of("Du hast " + targetPlayer.getName() + " eine Gildeneinladung gesendet."));

            targetPlayer.sendMessage(ChatAction.of("Du wurdest in die Gilde " + skyBlockGuild.getGuildName() + " eingeladen."));
            targetPlayer.sendMessage(ChatAction.of("Klicke hier um die Gildeneinladung anzunehmen.")
                    .hoverEvent(Component.text("§a<Linksklicke zum annehmen>"))
                    .clickEvent(ClickEvent.runCommand("/gilde accept")));
            targetPlayer.sendMessage(ChatAction.failure("Klicke hier um die Gildeneinladung abzulehnen.")
                    .hoverEvent(Component.text("§c<Linksklicke zum ablehnen>"))
                    .clickEvent(ClickEvent.runCommand("/gilde deny")));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {

            Player targetPlayer = Bukkit.getPlayer(args[1]);

            if (!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if (!SkyBlockPlugin.instance().getGuildManager().isLeaderOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du kannst als Gildenmitglied keine Spieler rauswerfen."));
                return;
            }

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            if (skyBlockGuild.getLeaderUniqueId().equals(targetPlayer.getUniqueId())) {
                player.sendMessage(ChatAction.failure("Dieser Spieler kann nicht aus der Gilde rausgeworfen werden."));
                return;
            }

            if (!skyBlockGuild.getGuildMembers().contains(targetPlayer.getUniqueId())) {
                player.sendMessage(ChatAction.failure("Dieser Spieler ist kein Gildenmitglied."));
                return;
            }

            SkyBlockPlugin.instance().getGuildManager().kickPlayerFromGuild(targetPlayer, skyBlockGuild);
            player.sendMessage(ChatAction.of(targetPlayer.getName() + " wurde aus deiner Gilde rausgeworfen."));
            targetPlayer.sendMessage(ChatAction.failure("Du wurdest aus deiner Gilde rausgeworfen."));
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {

            if (SkyBlockPlugin.instance().getGuildManager().isInGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in bereits in einer Gilde!"));
                return;
            }

            SkyBlockGuild invitedGuild = this.guildInviteMap.get(player);

            if (invitedGuild == null) {
                player.sendMessage(ChatAction.failure("Du wurdest in keine Gilde eingeladen."));
                return;
            }

            this.guildInviteMap.remove(player);
            SkyBlockPlugin.instance().getGuildManager().addPlayerToGuild(player, invitedGuild);
            player.sendMessage(ChatAction.of("Du bist der Gilde erfolgreich beigetreten."));

            for (UUID guildMember : invitedGuild.getGuildMembers()) {
                Player guildPlayer = Bukkit.getPlayer(guildMember);

                if (guildPlayer == null) continue;
                if (!guildPlayer.isOnline()) continue;
                if (guildPlayer.getUniqueId().equals(player.getUniqueId())) continue;

                guildPlayer.sendMessage(ChatAction.of(player.getName() + " ist deiner Gilde beigetreten."));
            }
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("deny")) {

            if (SkyBlockPlugin.instance().getGuildManager().isInGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in bereits in einer Gilde!"));
                return;
            }

            SkyBlockGuild invitedGuild = this.guildInviteMap.get(player);

            if (invitedGuild == null) {
                player.sendMessage(ChatAction.failure("Du wurdest in keine Gilde eingeladen."));
                return;
            }
            this.guildInviteMap.remove(player);
            player.sendMessage(ChatAction.of("Du hast die Gildeneinladung erfolgreich abgelehnt."));

            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {

            if (!SkyBlockPlugin.instance().getGuildManager().hasGuild(player)) {
                player.sendMessage(ChatAction.failure("Du besitzt oder befindest dich in keiner Gilde!"));
                return;
            }

            if (SkyBlockPlugin.instance().getGuildManager().isLeaderOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du bist Gildenbesitzer und kannst deine Gilde nur löschen."));
                return;
            }

            if (!SkyBlockPlugin.instance().getGuildManager().isMemberOfGuild(player)) {
                player.sendMessage(ChatAction.failure("Du befindest dich in keiner Gilde!"));
                return;
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);
            SkyBlockPlugin.instance().getGuildManager().kickPlayerFromGuild(player, skyBlockGuild);
            player.sendMessage(ChatAction.of("Du hast die Gilde erfolgreich verlassen."));

            for (UUID guildMember : skyBlockGuild.getGuildMembers()) {
                Player guildPlayer = Bukkit.getPlayer(guildMember);

                if (guildPlayer == null) continue;
                if (!guildPlayer.isOnline()) continue;

                guildPlayer.sendMessage(ChatAction.failure(player.getName() + " hat deine Gilde verlassen."));
            }
            return;
        }
    }
}
