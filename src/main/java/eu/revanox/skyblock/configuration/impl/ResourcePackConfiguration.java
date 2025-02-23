package eu.revanox.skyblock.configuration.impl;

import eu.revanox.skyblock.configuration.Configuration;
import eu.revanox.skyblock.configuration.annotation.ConfigPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResourcePackConfiguration extends Configuration {

    @ConfigPath("resource-pack.url")
    String url = "https://www.dropbox.com/s/7j7z7z7z7z7z7z7/resourcepack.zip?dl=1";

}
