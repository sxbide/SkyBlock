package mc.skyblock.plugin.island.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.island.model.SkyBlockIsland;

import java.util.UUID;

@Collection("skyblock_islands")
public interface IslandRepository extends Repository<SkyBlockIsland, UUID> {

    SkyBlockIsland findFirstByOwnerUniqueId(UUID ownerUniqueId);
}
