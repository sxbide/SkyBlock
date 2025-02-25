package mc.skyblock.plugin.user;

import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.tag.model.Tags;
import mc.skyblock.plugin.user.model.SkyBlockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(makeFinal = true)
public class UserManager {

    private Map<UUID, SkyBlockUser> uuidSkyBlockUserMap;
    private SkyBlockUserRepository repository;

    public UserManager() {
        this.uuidSkyBlockUserMap = new ConcurrentHashMap<>();
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(SkyBlockUserRepository.class);
    }

    public void loadUser(UUID uuid) {
        SkyBlockUser skyBlockUser = this.repository.findFirstById(uuid);

        if (skyBlockUser == null) {
            skyBlockUser = new SkyBlockUser();
            skyBlockUser.setUniqueId(uuid);
            skyBlockUser.setBalance(10);
            skyBlockUser.setGoldPieces(5);
            skyBlockUser.setSelectedTag(null);
            Map<Tags, Boolean> allTags = new ConcurrentHashMap<>();
            for (Tags tag : Tags.values()) {
                allTags.put(tag, false);
            }
            skyBlockUser.setTags(new ConcurrentHashMap<>(allTags));
            skyBlockUser.setEnderChests(new ArrayList<>(List.of(
                    "0".repeat(27),
                    "0".repeat(27),
                    "0".repeat(27)
            )));
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

    public void saveAll() {
        this.uuidSkyBlockUserMap.forEach((uuid, skyBlockUser) -> this.saveUser(uuid));
    }

    public void saveUser(UUID uuid) {
        SkyBlockUser skyBlockUser = this.uuidSkyBlockUserMap.get(uuid);

        if (skyBlockUser != null) {
            this.repository.save(skyBlockUser);
            this.uuidSkyBlockUserMap.remove(uuid);
        }
    }

    public void saveUser(SkyBlockUser skyBlockUser) {
        this.uuidSkyBlockUserMap.put(skyBlockUser.getUniqueId(), skyBlockUser);
        this.repository.save(skyBlockUser);
    }

    public void saveUser(UUID uuid, SkyBlockUser skyBlockUser) {
        this.uuidSkyBlockUserMap.put(uuid, skyBlockUser);
        this.repository.save(skyBlockUser);
    }

}
