package eu.revanox.skyblock.guild;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import eu.revanox.skyblock.guild.model.SkyBlockGuild;

import java.util.UUID;

@Collection("skyblock_guilds")
public interface SkyBlockGuildRepository extends Repository<SkyBlockGuild, UUID> {

    SkyBlockGuild findFirstByLeaderUniqueId(UUID leaderUniqueId);
    SkyBlockGuild findFirstByGuildName(String guildName);
}
