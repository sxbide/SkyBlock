package mc.skyblock.plugin.hologram.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.hologram.model.Hologram;

@Collection("holograms")
public interface HologramRepository extends Repository<Hologram, String> {
}
