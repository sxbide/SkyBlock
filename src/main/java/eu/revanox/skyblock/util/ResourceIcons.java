package eu.revanox.skyblock.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum ResourceIcons {

    GOLD_INGOT("\uE060");
    private String unicode;
}
