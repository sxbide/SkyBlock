package eu.revanox.skyblock.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum ResourceIcons {

    BALANCE_TAG_SCOREBOARD("\uE060"),
    ISLAND_TAG_SCOREBOARD("\uE061"),
    ONLINE_TAG_SCOREBOARD("\ue062"),
    SCOREBOARD_HEADER("\ue063"),
    OWNER_TAG_SCOREBOARD("\ue064"),
    VISITOR_TAG_SCOREBOARD("\ue065"),

    // TITLES
    SUMMER_2025_TITLE("\ue066"),

    // TABLIST
    SOCIALS_TAG_TABLIST("\ue067");

    private String unicode;
}
