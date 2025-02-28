package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.NumberUtil;
import mc.skyblock.plugin.util.custom.CustomSounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PayCommand extends AbstractCommand {

    public PayCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "pay", null);
    }

    @Override
    public void run(Player player, String[] args) {


        if(args.length == 2) {

            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if(targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            try {
                double amount = Double.parseDouble(args[1]);

                if (amount < 1.0 || args[1].startsWith("0.0")) {
                    player.sendMessage(ChatAction.failure("§cDer Betrag darf nicht kleiner als 1 ⛃ sein."));
                    return;
                }
                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

                if(skyBlockUser.getBalance() < amount) {
                    player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                    return;
                }


                SkyBlockUser targetUser = SkyBlockPlugin.instance().getUserManager().getUser(targetPlayer.getUniqueId());
                targetUser.addBalance(amount);
                skyBlockUser.removeBalance(amount);

                player.sendMessage(ChatAction.of("Du hast " + targetPlayer.getName() + " " + NumberUtil.formatBalance(amount) + " ⛃ überwiesen."));
                targetPlayer.sendMessage(ChatAction.of(player.getName() + " hat dir " + NumberUtil.formatBalance(amount) + " ⛃ überwiesen."));

                CustomSounds.CASHIER.playSound(player, 0.6F,1, player.getLocation());
                CustomSounds.CASHIER.playSound(targetPlayer, 0.6F,1, targetPlayer.getLocation());

            }catch (NumberFormatException ignored) {
                player.sendMessage(ChatAction.failure("§cBitte gebe einen korrekten Preis an."));
            }
            return;
        }
        player.sendMessage(ChatAction.failure("/pay <spielername> <betrag>"));
    }
}
