package eu.revanox.skyblock.location;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.location.model.Location;
import eu.revanox.skyblock.location.repository.LocationRepository;

public class LocationManager {

    LocationRepository positionRepository;

    public LocationManager() {
        this.positionRepository = SkyBlockPlugin.instance().getMongoManager().create(LocationRepository.class);
    }

    public Location getPosition(String name) {
        return this.positionRepository.findFirstById(name);
    }

    public void savePosition(Location position) {
        positionRepository.save(position);
    }

    public void deletePosition(Location position) {
        positionRepository.delete(position);
    }


}
