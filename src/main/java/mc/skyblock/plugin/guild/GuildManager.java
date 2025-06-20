package mc.skyblock.plugin.guild;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.guild.model.SkyBlockGuild;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GuildManager {

    private SkyBlockGuildRepository repository;
    private Map<UUID, SkyBlockGuild> guildMap;

    public GuildManager() {
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(SkyBlockGuildRepository.class);
        this.guildMap = new ConcurrentHashMap<>();

        for (SkyBlockGuild skyBlockGuild : this.repository.findAll()) {
            this.guildMap.put(skyBlockGuild.getUniqueId(), skyBlockGuild);
        }
    }

    public boolean existsGuild(String guildName) {
        boolean exists = false;
        for (SkyBlockGuild skyblockGuild : this.guildMap.values()) {
            if (skyblockGuild.getGuildName().equalsIgnoreCase(guildName)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public boolean isInGuild(Player player) {
        return hasGuild(player);
    }

    public boolean hasGuild(Player player) {
        for (SkyBlockGuild skyblockGuild : this.guildMap.values()) {
            if (skyblockGuild.getLeaderUniqueId().equals(player.getUniqueId()) || skyblockGuild.getGuildMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public SkyBlockGuild getGuild(Player player) {
        for (SkyBlockGuild skyblockGuild : this.guildMap.values()) {
            if (skyblockGuild.getLeaderUniqueId().equals(player.getUniqueId())) {
                return skyblockGuild;
            }
            if (skyblockGuild.getGuildMembers().contains(player.getUniqueId())) {
                return skyblockGuild;
            }
        }
        return null;
    }

    public boolean isMemberOfGuild(Player player) {
        for (SkyBlockGuild skyblockGuild : this.guildMap.values()) {
            return skyblockGuild.getGuildMembers().contains(player.getUniqueId());
        }
        return false;
    }

    public boolean isLeaderOfGuild(Player player) {
        for (SkyBlockGuild skyblockGuild : this.guildMap.values()) {
            return skyblockGuild.getLeaderUniqueId().equals(player.getUniqueId());
        }
        return false;
    }

    public void createGuild(Player player, String guildName) {
        if (hasGuild(player)) return;

        SkyBlockGuild skyBlockGuild = new SkyBlockGuild();
        UUID randomUUID = UUID.randomUUID();
        skyBlockGuild.setUniqueId(randomUUID);
        skyBlockGuild.setGuildName(guildName);
        skyBlockGuild.setCreatedAt(System.currentTimeMillis());
        skyBlockGuild.setMaxMembers(5);
        skyBlockGuild.setLeaderUniqueId(player.getUniqueId());
        skyBlockGuild.setGuildMembers(new ArrayList<>() {{
            add(player.getUniqueId());
        }});

        this.guildMap.put(randomUUID, skyBlockGuild);
        this.repository.save(skyBlockGuild);
    }

    public void kickPlayerFromGuild(UUID uniqueId, SkyBlockGuild skyBlockGuild) {
        skyBlockGuild.getGuildMembers().remove(uniqueId);
        this.guildMap.put(skyBlockGuild.getUniqueId(), skyBlockGuild);
        this.repository.save(skyBlockGuild);
    }

    public void addPlayerToGuild(Player player, SkyBlockGuild skyBlockGuild) {
        skyBlockGuild.getGuildMembers().add(player.getUniqueId());
        this.guildMap.put(skyBlockGuild.getUniqueId(), skyBlockGuild);
        this.repository.save(skyBlockGuild);
    }

    public void deleteGuild(Player player) {
        if (!hasGuild(player)) return;

        SkyBlockGuild skyBlockGuild = this.repository.findFirstByLeaderUniqueId(player.getUniqueId());
        if (!skyBlockGuild.getLeaderUniqueId().equals(player.getUniqueId())) return;

        this.guildMap.remove(skyBlockGuild.getUniqueId());
        this.repository.delete(skyBlockGuild);
    }
}
