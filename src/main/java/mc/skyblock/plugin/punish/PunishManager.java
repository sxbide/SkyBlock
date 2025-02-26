package mc.skyblock.plugin.punish;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.punish.configuration.PunishConfiguration;
import mc.skyblock.plugin.punish.model.ban.Ban;
import mc.skyblock.plugin.punish.model.mute.Mute;
import mc.skyblock.plugin.punish.model.reason.PunishReason;
import mc.skyblock.plugin.punish.model.warn.Warn;
import mc.skyblock.plugin.punish.repository.ban.BanRepository;
import mc.skyblock.plugin.punish.repository.mute.MuteRepository;
import mc.skyblock.plugin.punish.repository.warn.WarnRepository;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishManager {

    PunishConfiguration configuration;

    BanRepository banRepository;
    MuteRepository muteRepository;
    WarnRepository warnRepository;

    Map<UUID, List<Warn>> warns;
    Map<UUID, List<Mute>> mutes;
    Map<UUID, List<Ban>> bans;

    public PunishManager(PunishConfiguration configuration) {
        this.configuration = configuration;

        this.banRepository = SkyBlockPlugin.instance().getMongoManager().create(BanRepository.class);
        this.muteRepository = SkyBlockPlugin.instance().getMongoManager().create(MuteRepository.class);
        this.warnRepository = SkyBlockPlugin.instance().getMongoManager().create(WarnRepository.class);

        this.warns = this.warnRepository.findAll().stream().collect(Collectors.groupingBy(Warn::getWarnedPlayer));
        this.mutes = this.muteRepository.findAll().stream().collect(Collectors.groupingBy(Mute::getMutedPlayer));
        this.bans = this.banRepository.findAll().stream().collect(Collectors.groupingBy(Ban::getBannedPlayer));
    }

    public List<Warn> getWarns(UUID player) {
        return this.warns.get(player);
    }

    public List<Mute> getMutes(UUID player) {
        return this.mutes.get(player);
    }

    public List<Ban> getBans(UUID player) {
        return this.bans.get(player);
    }

    public boolean hasActiveWarns(UUID player) {
        return this.warns.get(player).stream().anyMatch(Warn::isActive);
    }

    public boolean hasActiveMutes(UUID player) {
        return this.mutes.get(player).stream().anyMatch(Mute::isActive);
    }

    public boolean hasActiveBans(UUID player) {
        return this.bans.get(player).stream().anyMatch(Ban::isActive);
    }

    public void ban(UUID bannedPlayer, UUID bannedBy, PunishReason reason) {
        Ban ban = new Ban();
        ban.setBannedPlayer(bannedPlayer);
        ban.setBannedBy(bannedBy);
        ban.setReason(reason);
        ban.setBannedAt(LocalDateTime.now());
        LocalDateTime expireAt = reason.isPermanent() ? null : LocalDateTime.now().plusSeconds(reason.getDuration().getSeconds());
        ban.setExpireAt(expireAt);

        this.banRepository.save(ban);
        this.bans.putIfAbsent(bannedPlayer, new ArrayList<>());
        this.bans.get(bannedPlayer).add(ban);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(bannedPlayer);
        if (player != null) {
            //TODO: Ban screen
        }
    }

    public void mute(UUID mutedPlayer, UUID mutedBy, PunishReason reason) {
        Mute mute = new Mute();
        mute.setMutedPlayer(mutedPlayer);
        mute.setMutedBy(mutedBy);
        mute.setReason(reason);
        mute.setMutedAt(LocalDateTime.now());
        LocalDateTime expireAt = reason.isPermanent() ? null : LocalDateTime.now().plusSeconds(reason.getDuration().getSeconds());
        mute.setExpireAt(expireAt);

        this.muteRepository.save(mute);
        this.mutes.putIfAbsent(mutedPlayer, new ArrayList<>());
        this.mutes.get(mutedPlayer).add(mute);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(mutedPlayer);
        if (player != null) {
            //TODO: Mute screen
        }
    }

    public void warn(UUID warnedPlayer, UUID warnedBy, String reason, LocalDateTime expireAt) {
        Warn warn = new Warn();
        warn.setWarnedPlayer(warnedPlayer);
        warn.setWarnedBy(warnedBy);
        warn.setReason(reason);
        warn.setWarnedAt(LocalDateTime.now());
        warn.setExpireAt(expireAt);

        this.warnRepository.save(warn);
        this.warns.putIfAbsent(warnedPlayer, new ArrayList<>());
        this.warns.get(warnedPlayer).add(warn);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(warnedPlayer);
        if (player != null) {
            //TODO: Warn screen
        }
    }

    public void warn(UUID warnedPlayer, UUID warnedBy, String reason) {
        this.warn(warnedPlayer, warnedBy, reason, null);
    }

    public void removeWarn(UUID warnId) {
        Warn warn = this.warns.values().stream().flatMap(List::stream).filter(w -> w.getWarnId().equals(warnId)).findFirst().orElse(null);
        if (warn == null) {
            return;
        }
        this.warnRepository.delete(warn);
        this.warns.get(warn.getWarnedPlayer()).remove(warn);
    }

    public void removeMute(UUID muteId) {
        Mute mute = this.mutes.values().stream().flatMap(List::stream).filter(m -> m.getMuteId().equals(muteId)).findFirst().orElse(null);
        if (mute == null) {
            return;
        }
        this.muteRepository.delete(mute);
        this.mutes.get(mute.getMutedPlayer()).remove(mute);
    }

    public void removeBan(UUID banId) {
        Ban ban = this.bans.values().stream().flatMap(List::stream).filter(b -> b.getBanId().equals(banId)).findFirst().orElse(null);
        if (ban == null) {
            return;
        }
        this.banRepository.delete(ban);
        this.bans.get(ban.getBannedPlayer()).remove(ban);
    }

    public void removeWarns(UUID player) {
        List<Warn> warns = this.warns.get(player);
        if (warns == null) {
            return;
        }
        this.warnRepository.deleteMany(warns);
        this.warns.remove(player);
    }

    public void removeMutes(UUID player) {
        List<Mute> mutes = this.mutes.get(player);
        if (mutes == null) {
            return;
        }
        this.muteRepository.deleteMany(mutes);
        this.mutes.remove(player);
    }

    public void removeBans(UUID player) {
        List<Ban> bans = this.bans.get(player);
        if (bans == null) {
            return;
        }
        this.banRepository.deleteMany(bans);
        this.bans.remove(player);
    }

    public void removePunishments(UUID player) {
        this.removeWarns(player);
        this.removeMutes(player);
        this.removeBans(player);
    }

}
