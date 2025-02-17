package eu.revanox.skyblock.tablist;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.util.builder.TeamBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.group.GroupDataRecalculateEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TablistManager {

    Scoreboard scoreboard;
    Map<String, Team> teams;

    public TablistManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.teams = new LinkedHashMap<>();

        this.updateTeams();

        EventBus eventBus = SkyBlockPlugin.instance().getLuckPerms().getEventBus();

        eventBus.subscribe(UserDataRecalculateEvent.class, userDataRecalculateEvent -> {
            var player = Bukkit.getPlayer(userDataRecalculateEvent.getUser().getUniqueId());
            if (player != null) {
                this.setTablist(player);
            }
        });

        eventBus.subscribe(GroupDataRecalculateEvent.class, groupCreateEvent -> this.updateTeams());


    }

    private void updateTeams() {
        int i = 1;
        for (Group loadedGroup : getGroupsSortedByWeight()) {

            String before = (i <= 9 ? "0" + i : String.valueOf(i));
            i++;
            String prefix = loadedGroup.getCachedData().getMetaData().getPrefix();
            if (prefix == null) prefix = loadedGroup.getName();


            TeamBuilder team = TeamBuilder.builder()
                    .name(before + loadedGroup.getName().toLowerCase())
                    .prefix(MiniMessage.miniMessage().deserialize(prefix).appendSpace());

            if (!team.exists(this.scoreboard)) {
                this.teams.put(loadedGroup.getName(), team.create(this.scoreboard));
                System.out.println("Registered Team: " + loadedGroup.getName() + ":" + this.teams.get(loadedGroup.getName()).getName());
            }


        }
    }

    public void setTablist(Player player) {
        player.setScoreboard(this.scoreboard);

        String playerGroup = SkyBlockPlugin.instance().getLuckPerms().getUserManager()
                .getUser(player.getUniqueId())
                .getPrimaryGroup();

        for (var groupName : this.teams.keySet()) {
            if (groupName.equalsIgnoreCase(playerGroup)) {
                System.out.println(groupName + ":" + playerGroup);
                var team = this.teams.get(groupName);
                team.addPlayer(player);
                return;
            }
            var team = this.teams.get("default");
            team.addPlayer(player);

        }
    }

    private List<Group> getGroupsSortedByWeight() {
        return SkyBlockPlugin.instance().getLuckPerms().getGroupManager().getLoadedGroups()
                .stream()
                .sorted(Comparator.comparingInt(this::getGroupWeight))
                .collect(Collectors.toList());
    }

    public Team getTeamByPlayer(Player player) {
        String playerGroup = SkyBlockPlugin.instance().getLuckPerms().getUserManager()
                .getUser(player.getUniqueId())
                .getPrimaryGroup();

        return this.teams.getOrDefault(playerGroup, this.teams.get("default"));
    }


    private int getGroupWeight(Group group) {
        var weight = group.getWeight();
        if (weight.isPresent()) {
            return weight.getAsInt();
        }
        return 99;
    }
}
