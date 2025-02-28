package mc.skyblock.plugin.hologram.repository;

import mc.skyblock.plugin.hologram.model.Hologram;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("holograms")
public interface HologramRepository extends Repository<Hologram, String> {
}
