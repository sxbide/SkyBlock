package mc.skyblock.plugin.punish.repository.warn;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.punish.model.mute.Mute;
import mc.skyblock.plugin.punish.model.warn.Warn;

import java.util.UUID;

@Collection("punishments_warns")
public interface WarnRepository extends Repository<Warn, UUID> {
}
