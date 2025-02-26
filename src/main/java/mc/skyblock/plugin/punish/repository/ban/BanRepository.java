package mc.skyblock.plugin.punish.repository.ban;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.punish.model.ban.Ban;

import java.util.UUID;

@Collection("punishments_bans")
public interface BanRepository extends Repository<Ban, UUID> {
}
