package eu.revanox.skyblock.island.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import eu.revanox.skyblock.island.model.SkyBlockIsland;

import java.util.UUID;

@Collection("skyblock_islands")
public interface IslandRepository extends Repository<SkyBlockIsland, UUID> {

    SkyBlockIsland findFirstByOwnerUniqueId(UUID ownerUniqueId);
}
