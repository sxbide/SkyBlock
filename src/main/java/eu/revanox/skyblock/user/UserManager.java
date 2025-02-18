package eu.revanox.skyblock.user;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(makeFinal = true)
public class UserManager {

    private Map<UUID, SkyBlockUser> uuidSkyBlockUserMap;
    private SkyBlockUserRepository repository;

    public UserManager() {
        this.uuidSkyBlockUserMap = new HashMap<>();
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(SkyBlockUserRepository.class);
    }

    public void updateEntry(UUID uuid, SkyBlockUser user) {
        this.uuidSkyBlockUserMap.put(uuid, user);
    }

    public void loadUser(UUID uuid) {
        SkyBlockUser skyBlockUser = this.repository.findFirstById(uuid);

        if (skyBlockUser == null) {
            skyBlockUser = new SkyBlockUser();
            skyBlockUser.setUniqueId(uuid);
            skyBlockUser.setBalance(0);
            skyBlockUser.setGoldPieces(0);
            skyBlockUser.setSelectedTag(null);
            skyBlockUser.setTags(new ArrayList<>());
        }

        this.uuidSkyBlockUserMap.put(uuid, skyBlockUser);
    }

    public SkyBlockUser getUser(UUID uuid) {
        SkyBlockUser skyBlockUser = this.uuidSkyBlockUserMap.get(uuid);

        if (skyBlockUser == null) {
            skyBlockUser = this.repository.findFirstById(uuid);
        }

        return skyBlockUser;
    }

    public void saveUser(UUID uuid) {
        SkyBlockUser skyBlockUser = this.uuidSkyBlockUserMap.get(uuid);

        if (skyBlockUser != null) {
            this.repository.save(skyBlockUser);
            this.uuidSkyBlockUserMap.remove(uuid);
        }
    }

    public void saveUser(SkyBlockUser skyBlockUser) {
        this.repository.save(skyBlockUser);
    }
}
