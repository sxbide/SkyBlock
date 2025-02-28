package mc.skyblock.plugin.warps;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.warps.model.Warp;
import mc.skyblock.plugin.warps.repository.WarpRepository;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WarpManager {

    WarpRepository warpRepository;
    Map<String, Warp> warps;

    public WarpManager() {
        this.warpRepository = SkyBlockPlugin.instance().getMongoManager().create(WarpRepository.class);
        this.warps = warpRepository.findAll().stream().collect(Collectors.toMap(Warp::getName, Function.identity()));
    }

    public void addWarp(Warp warp) {
        warps.put(warp.getName(), warp);
    }

    public void removeWarp(Warp warp) {
        warps.remove(warp.getName());
    }

    public Warp getWarp(String name) {
        return warps.get(name);
    }

    public boolean canAccessWarp(Player player, Warp warp) {
        String primaryGroup = SkyBlockPlugin.instance().getLuckPerms().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        Group group = SkyBlockPlugin.instance().getLuckPerms().getGroupManager().getGroup(primaryGroup);

        if (group == null && warp.getNeededRank() != null) {
            return false;
        }
        Objects.requireNonNull(group, "Player does not have a primary group; LuckPerms issue");
        SkyBlockUser user = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

        if (warp.isBuyable() && !user.hasWarp(warp.getName())) {
            return false;
        }

        if (warp.getNeededRank() == null && !warp.isBuyable()) {
            return true;
        }
        return group.getName().equalsIgnoreCase(warp.getNeededRank());
    }

    public void saveAll() {
        warpRepository.saveAll(warps.values());
    }

}
