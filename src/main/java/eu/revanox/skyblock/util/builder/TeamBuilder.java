package eu.revanox.skyblock.util.builder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamBuilder {

    String name;
    Component prefix;

    public static TeamBuilder builder() {
        return new TeamBuilder();
    }

    public TeamBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder prefix(Component prefix) {
        this.prefix = prefix;
        return this;
    }

    public Team create(Scoreboard scoreboard) {
        var team = scoreboard.registerNewTeam(this.name);
        team.prefix(this.prefix);
        team.color(NamedTextColor.GRAY);
        return team;
    }

    public boolean exists(Scoreboard scoreboard) {
        return scoreboard.getTeam(name) != null;
    }


}
