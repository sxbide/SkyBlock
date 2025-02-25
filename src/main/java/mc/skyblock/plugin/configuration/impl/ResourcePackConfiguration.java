package mc.skyblock.plugin.configuration.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResourcePackConfiguration extends Configuration {

    @ConfigPath("resource-pack.url")
    String url = "empty";

}
