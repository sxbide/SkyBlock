package mc.skyblock.plugin.warps.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.warps.model.Warp;

@Collection("warps")
public interface WarpRepository extends Repository<Warp, String> {
}
