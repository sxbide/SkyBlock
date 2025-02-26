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
import mc.skyblock.plugin.util.TimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PunishManager {

    private final PunishConfiguration configuration;
    private final BanRepository banRepository;
    private final MuteRepository muteRepository;
    private final WarnRepository warnRepository;

    private final Map<UUID, List<Warn>> warns;
    private final Map<UUID, List<Mute>> mutes;
    private final Map<UUID, List<Ban>> bans;

    public PunishManager(PunishConfiguration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration cannot be null");

        this.banRepository = SkyBlockPlugin.instance().getMongoManager().create(BanRepository.class);
        this.muteRepository = SkyBlockPlugin.instance().getMongoManager().create(MuteRepository.class);
        this.warnRepository = SkyBlockPlugin.instance().getMongoManager().create(WarnRepository.class);

        this.warns = Optional.of(this.warnRepository.findAll().stream().collect(Collectors.groupingBy(Warn::getWarnedPlayer)))
                .orElse(new ConcurrentHashMap<>());
        this.mutes = Optional.of(this.muteRepository.findAll().stream().collect(Collectors.groupingBy(Mute::getMutedPlayer)))
                .orElse(new ConcurrentHashMap<>());
        this.bans = Optional.of(this.banRepository.findAll().stream().collect(Collectors.groupingBy(Ban::getBannedPlayer)))
                .orElse(new ConcurrentHashMap<>());
    }

    public List<Warn> getWarns(UUID player) {
        return this.warns.getOrDefault(player, Collections.emptyList());
    }

    public List<Mute> getMutes(UUID player) {
        return this.mutes.getOrDefault(player, Collections.emptyList());
    }

    public List<Ban> getBans(UUID player) {
        return this.bans.getOrDefault(player, Collections.emptyList());
    }

    public boolean hasActiveWarns(UUID player) {
        return this.warns.getOrDefault(player, Collections.emptyList()).stream().anyMatch(Warn::isActive);
    }

    public boolean hasActiveMutes(UUID player) {
        return this.mutes.getOrDefault(player, Collections.emptyList()).stream().anyMatch(Mute::isActive);
    }

    public boolean hasActiveBans(UUID player) {
        return this.bans.getOrDefault(player, Collections.emptyList()).stream().anyMatch(Ban::isActive);
    }

    public void ban(UUID bannedPlayer, UUID bannedBy, PunishReason reason) {
        Objects.requireNonNull(bannedPlayer, "bannedPlayer cannot be null");
        Objects.requireNonNull(bannedBy, "bannedBy cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");

        Ban ban = new Ban();
        ban.setBannedPlayer(bannedPlayer);
        ban.setBannedBy(bannedBy);
        ban.setReason(reason);
        ban.setBannedAt(LocalDateTime.now());
        LocalDateTime expireAt = reason.isPermanent() ? null : LocalDateTime.now().plusSeconds(reason.getDuration().getSeconds());
        ban.setExpireAt(expireAt);

        this.banRepository.save(ban);
        this.bans.computeIfAbsent(bannedPlayer, k -> new ArrayList<>()).add(ban);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(bannedPlayer);
        if (player != null) {
            player.kick(this.getBanScreen(ban));
        }
    }

    public void mute(UUID mutedPlayer, UUID mutedBy, PunishReason reason) {
        Objects.requireNonNull(mutedPlayer, "mutedPlayer cannot be null");
        Objects.requireNonNull(mutedBy, "mutedBy cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");

        Mute mute = new Mute();
        mute.setMutedPlayer(mutedPlayer);
        mute.setMutedBy(mutedBy);
        mute.setReason(reason);
        mute.setMutedAt(LocalDateTime.now());
        LocalDateTime expireAt = reason.isPermanent() ? null : LocalDateTime.now().plusSeconds(reason.getDuration().getSeconds());
        mute.setExpireAt(expireAt);

        this.muteRepository.save(mute);
        this.mutes.computeIfAbsent(mutedPlayer, k -> new ArrayList<>()).add(mute);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(mutedPlayer);
        if (player != null) {
            player.sendMessage(this.getMuteScreen(mute));
        }
    }

    public void warn(UUID warnedPlayer, UUID warnedBy, String reason, LocalDateTime expireAt) {
        Objects.requireNonNull(warnedPlayer, "warnedPlayer cannot be null");
        Objects.requireNonNull(warnedBy, "warnedBy cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");

        Warn warn = new Warn();
        warn.setWarnedPlayer(warnedPlayer);
        warn.setWarnedBy(warnedBy);
        warn.setReason(reason);
        warn.setWarnedAt(LocalDateTime.now());
        warn.setExpireAt(expireAt);

        this.warnRepository.save(warn);
        this.warns.computeIfAbsent(warnedPlayer, k -> new ArrayList<>()).add(warn);

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(warnedPlayer);
        if (player != null) {
            player.sendMessage(this.getWarnScreen(warn));
        }
    }

    public void warn(UUID warnedPlayer, UUID warnedBy, String reason) {
        this.warn(warnedPlayer, warnedBy, reason, null);
    }

    public void kick(UUID kickedPlayer, UUID kickedBy, String reason) {
        Objects.requireNonNull(kickedPlayer, "kickedPlayer cannot be null");
        Objects.requireNonNull(kickedBy, "kickedBy cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");

        Player player = SkyBlockPlugin.instance().getServer().getPlayer(kickedPlayer);
        if (player != null) {
            player.kick(this.getKickScreen(kickedPlayer, kickedBy, reason));
        }
    }

    public void warn(UUID warnedPlayer, UUID warnedBy, LocalDateTime expireAt) {
        this.warn(warnedPlayer, warnedBy, "No reason given", expireAt);
    }

    public void removeWarn(UUID warnId) {
        Objects.requireNonNull(warnId, "warnId cannot be null");

        Warn warn = this.warns.values().stream().flatMap(List::stream).filter(w -> w.getWarnId().equals(warnId)).findFirst().orElse(null);
        if (warn == null) {
            return;
        }
        this.warnRepository.delete(warn);
        this.warns.getOrDefault(warn.getWarnedPlayer(), Collections.emptyList()).remove(warn);
    }

    public void removeMute(UUID muteId) {
        Objects.requireNonNull(muteId, "muteId cannot be null");

        Mute mute = this.mutes.values().stream().flatMap(List::stream).filter(m -> m.getMuteId().equals(muteId)).findFirst().orElse(null);
        if (mute == null) {
            return;
        }
        this.muteRepository.delete(mute);
        this.mutes.getOrDefault(mute.getMutedPlayer(), Collections.emptyList()).remove(mute);
    }

    public void removeBan(UUID banId) {
        Objects.requireNonNull(banId, "banId cannot be null");

        Ban ban = this.bans.values().stream().flatMap(List::stream).filter(b -> b.getBanId().equals(banId)).findFirst().orElse(null);
        if (ban == null) {
            return;
        }
        this.banRepository.delete(ban);
        this.bans.getOrDefault(ban.getBannedPlayer(), Collections.emptyList()).remove(ban);
    }

    public void removeWarns(UUID player) {
        Objects.requireNonNull(player, "player cannot be null");

        List<Warn> warns = this.warns.get(player);
        if (warns == null) {
            return;
        }
        this.warnRepository.deleteMany(warns);
        this.warns.remove(player);
    }

    public void removeMutes(UUID player) {
        Objects.requireNonNull(player, "player cannot be null");

        List<Mute> mutes = this.mutes.get(player);
        if (mutes == null) {
            return;
        }
        this.muteRepository.deleteMany(mutes);
        this.mutes.remove(player);
    }

    public void removeBans(UUID player) {
        Objects.requireNonNull(player, "player cannot be null");

        List<Ban> bans = this.bans.get(player);
        if (bans == null) {
            return;
        }
        this.banRepository.deleteMany(bans);
        this.bans.remove(player);
    }

    public void removePunishments(UUID player) {
        Objects.requireNonNull(player, "player cannot be null");

        this.removeWarns(player);
        this.removeMutes(player);
        this.removeBans(player);
    }

    @ApiStatus.Internal
    public Component getBanScreen(Ban ban) {
        Objects.requireNonNull(ban, "ban cannot be null");

        return createScreen(this.configuration.getBanScreen(), Map.of(
                "{id}", ban.getBanId().toString(),
                "{reason}", ban.getReason().getName(),
                "{bannedBy}", Bukkit.getOfflinePlayer(ban.getBannedBy()).getName(),
                "{bannedAt}", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ban.getBannedAt()),
                "{expireAt}", ban.isPermanent() ? "Permanent" : TimeUtil.formatTime(LocalDateTime.now(), ban.getExpireAt(), true)
        ));
    }

    @ApiStatus.Internal
    public Component getMuteScreen(Mute mute) {
        Objects.requireNonNull(mute, "mute cannot be null");

        return createScreen(this.configuration.getMuteScreen(), Map.of(
                "{id}", mute.getMuteId().toString(),
                "{reason}", mute.getReason().getName(),
                "{mutedBy}", Bukkit.getOfflinePlayer(mute.getMutedBy()).getName(),
                "{mutedAt}", new SimpleDateFormat("dd.MM.yyyy, HH:mm").format(mute.getMutedAt()),
                "{expireAt}", mute.isPermanent() ? "Permanent" : TimeUtil.formatTime(LocalDateTime.now(), mute.getExpireAt(), true)
        ));
    }

    @ApiStatus.Internal
    public Component getWarnScreen(Warn warn) {
        Objects.requireNonNull(warn, "warn cannot be null");

        return createScreen(this.configuration.getWarnScreen(), Map.of(
                "{id}", warn.getWarnId().toString(),
                "{reason}", warn.getReason(),
                "{warnedBy}", Bukkit.getOfflinePlayer(warn.getWarnedBy()).getName(),
                "{warnedAt}", new SimpleDateFormat("dd.MM.yyyy, HH:mm").format(warn.getWarnedAt()),
                "{expireAt}", warn.isPermanent() ? "Permanent" : TimeUtil.formatTime(LocalDateTime.now(), warn.getExpireAt(), true)
        ));
    }

    @ApiStatus.Internal
    public Component getKickScreen(UUID kickedPlayer, UUID kickedBy, String reason) {
        Objects.requireNonNull(kickedPlayer, "kickedPlayer cannot be null");
        Objects.requireNonNull(kickedBy, "kickedBy cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");

        return createScreen(this.configuration.getKickScreen(), Map.of(
                "{reason}", reason,
                "{kickedBy}", Bukkit.getOfflinePlayer(kickedBy).getName()
        ));
    }

    private Component createScreen(List<String> template, Map<String, String> replacements) {
        Objects.requireNonNull(template, "template cannot be null");
        Objects.requireNonNull(replacements, "replacements cannot be null");

        List<String> replaced = template.stream()
                .map(s -> {
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        s = s.replace(entry.getKey(), entry.getValue());
                    }
                    return s;
                })
                .toList();
        return replaced.stream()
                .map(MiniMessage.miniMessage()::deserialize)
                .reduce((s1, s2) -> s1.appendNewline().append(s2))
                .orElse(Component.empty());
    }
}