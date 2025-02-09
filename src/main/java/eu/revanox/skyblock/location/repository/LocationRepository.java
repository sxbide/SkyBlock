package eu.revanox.skyblock.location.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import eu.revanox.skyblock.location.model.Location;

@Collection("locations")
public interface LocationRepository extends Repository<Location, String> {

}
