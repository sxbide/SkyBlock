package mc.skyblock.plugin.location;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.location.model.Location;
import mc.skyblock.plugin.location.repository.LocationRepository;

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
