package mc.skyblock.plugin.caseopening.mongo.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.caseopening.mongo.model.Case;

@Collection("case")
public interface CaseRepository extends Repository<Case, Integer> {
}
