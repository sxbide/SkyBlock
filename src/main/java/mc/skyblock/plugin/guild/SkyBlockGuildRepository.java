package mc.skyblock.plugin.guild;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.guild.model.SkyBlockGuild;

import java.util.UUID;

@Collection("skyblock_guilds")
public interface SkyBlockGuildRepository extends Repository<SkyBlockGuild, UUID> {

    SkyBlockGuild findFirstByLeaderUniqueId(UUID leaderUniqueId);

    SkyBlockGuild findFirstByGuildName(String guildName);
}
