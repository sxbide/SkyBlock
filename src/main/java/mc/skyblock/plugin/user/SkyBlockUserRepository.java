package mc.skyblock.plugin.user;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.user.model.SkyBlockUser;

import java.util.UUID;

@Collection("skyblock_users")
public interface SkyBlockUserRepository extends Repository<SkyBlockUser, UUID> {
}
