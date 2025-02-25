package mc.skyblock.plugin.location.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.location.model.Location;

@Collection("locations")
public interface LocationRepository extends Repository<Location, String> {

}
