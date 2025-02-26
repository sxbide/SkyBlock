package mc.skyblock.plugin.punish.repository.mute;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.punish.model.ban.Ban;
import mc.skyblock.plugin.punish.model.mute.Mute;

import java.util.UUID;

@Collection("punishments_mutes")
public interface MuteRepository extends Repository<Mute, UUID> {
}
