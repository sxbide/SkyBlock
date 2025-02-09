package eu.revanox.skyblock.user;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import eu.revanox.skyblock.user.model.SkyBlockUser;

import java.util.UUID;

@Collection("skyblock_users")
public interface SkyBlockUserRepository extends Repository<SkyBlockUser, UUID> {
}
