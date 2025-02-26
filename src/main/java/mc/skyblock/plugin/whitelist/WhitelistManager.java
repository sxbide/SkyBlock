package mc.skyblock.plugin.whitelist;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.whitelist.configuration.WhitelistConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.UUID;

public class WhitelistManager {

    WhitelistConfiguration whitelistConfiguration;

    public WhitelistManager(WhitelistConfiguration whitelistConfiguration) {
        this.whitelistConfiguration = whitelistConfiguration;
    }

    public boolean isWhitelisted(UUID uuid) {
        return whitelistConfiguration.getWhitelistedPlayers().contains(uuid);
    }

    public boolean status() {
        return whitelistConfiguration.isEnabled();
    }

    public void status(boolean status) {
        whitelistConfiguration.setEnabled(status);
        Configuration.save(SkyBlockPlugin.instance().getDataFolder().toPath().resolve("whitelist.json").toString(), whitelistConfiguration);
    }

    public void whitelist(UUID uuid) {
        whitelistConfiguration.getWhitelistedPlayers().add(uuid);
        Configuration.save(SkyBlockPlugin.instance().getDataFolder().toPath().resolve("whitelist.json").toString(), whitelistConfiguration);
    }

    public void unwhitelist(UUID uuid) {
        whitelistConfiguration.getWhitelistedPlayers().remove(uuid);
        Configuration.save(SkyBlockPlugin.instance().getDataFolder().toPath().resolve("whitelist.json").toString(), whitelistConfiguration);
    }

    public List<UUID> whitelistedPlayers() {
        return whitelistConfiguration.getWhitelistedPlayers();
    }

    public boolean isEnabled() {
        return whitelistConfiguration.isEnabled();
    }

    public Component getKickMessage() {
        return whitelistConfiguration.getWhiteListKickMessage().stream().map(MiniMessage.miniMessage()::deserialize).reduce((s1, s2) -> s1.appendNewline().append(s2)).orElse(ChatAction.failure("No kick message found."));
    }

}
