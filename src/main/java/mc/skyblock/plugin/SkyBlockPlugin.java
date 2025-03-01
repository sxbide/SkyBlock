package mc.skyblock.plugin;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.auctions.AuctionsManager;
import mc.skyblock.plugin.caseopening.CaseOpeningManager;
import mc.skyblock.plugin.codec.ItemStackCodec;
import mc.skyblock.plugin.codec.LocationCodec;
import mc.skyblock.plugin.command.CommandManager;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.impl.ResourcePackConfiguration;
import mc.skyblock.plugin.cosmetic.CosmeticManager;
import mc.skyblock.plugin.guild.GuildManager;
import mc.skyblock.plugin.hologram.HologramManager;
import mc.skyblock.plugin.island.IslandManager;
import mc.skyblock.plugin.location.LocationManager;
import mc.skyblock.plugin.punish.PunishManager;
import mc.skyblock.plugin.punish.configuration.PunishConfiguration;
import mc.skyblock.plugin.scoreboard.ScoreboardManager;
import mc.skyblock.plugin.shop.ShopManager;
import mc.skyblock.plugin.tablist.TablistManager;
import mc.skyblock.plugin.tag.TagManager;
import mc.skyblock.plugin.user.UserManager;
import mc.skyblock.plugin.warps.WarpManager;
import mc.skyblock.plugin.whitelist.WhitelistManager;
import mc.skyblock.plugin.whitelist.configuration.WhitelistConfiguration;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bson.codecs.Codec;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SkyBlockPlugin extends JavaPlugin {

    static SkyBlockPlugin instance;

    ParticleNativeAPI particleAPI;

    ResourcePackConfiguration resourcePackConfiguration;
    WhitelistConfiguration whitelistConfiguration;
    PunishConfiguration punishConfiguration;

    MongoManager mongoManager;
    UserManager userManager;
    ScoreboardManager scoreboardManager;
    IslandManager islandManager;
    LocationManager locationManager;
    AuctionsManager auctionsManager;
    LuckPerms luckPerms;
    InventoryManager inventoryManager;
    TablistManager tablistManager;
    CommandManager commandManager;
    TagManager tagManager;
    GuildManager guildManager;
    CaseOpeningManager caseOpeningManager;
    WhitelistManager whitelistManager;
    HologramManager hologramManager;
    PunishManager punishManager;
    WarpManager warpManager;
    ShopManager shopManager;
    CosmeticManager cosmeticManager;


    public static SkyBlockPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.resourcePackConfiguration = Configuration.load(getDataFolder().toPath().resolve("resourcepack.json").toString(), ResourcePackConfiguration.class);
        this.whitelistConfiguration = Configuration.load(getDataFolder().toPath().resolve("whitelist.json").toString(), WhitelistConfiguration.class);
        this.punishConfiguration = Configuration.load(getDataFolder().toPath().resolve("punish.json").toString(), PunishConfiguration.class);

        this.luckPerms = LuckPermsProvider.get();

        this.particleAPI = ParticleNativeCore.loadAPI(this);

        this.mongoManager = new MongoManager(Credentials.of("mongodb://keinepixel:rc8Saw8UNU4Dp+8UuWOUuVWZnJOEwp2nYhOcvqf5L@87.106.178.7:27017/", "skyblock"));

        Reflections codecReflections = new Reflections("mc.skyblock.plugin.codec");
        codecReflections.getSubTypesOf(Codec.class).forEach(codec -> {
            try {
                this.mongoManager.registerCodec(codec.getDeclaredConstructor().newInstance());
                getLogger().info("Registered codec: " + codec.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        this.userManager = new UserManager();
        this.scoreboardManager = new ScoreboardManager(this);
        this.islandManager = new IslandManager();
        this.locationManager = new LocationManager();
        this.auctionsManager = new AuctionsManager();
        this.tablistManager = new TablistManager();
        this.commandManager = new CommandManager(this);
        this.tagManager = new TagManager();
        this.guildManager = new GuildManager();
        this.caseOpeningManager = new CaseOpeningManager();
        this.whitelistManager = new WhitelistManager(this.whitelistConfiguration);
        this.punishManager = new PunishManager(this.punishConfiguration);
        this.hologramManager = new HologramManager();
        this.warpManager = new WarpManager();
        this.shopManager = new ShopManager();
        this.cosmeticManager = new CosmeticManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.invoke();


        Reflections listenerReflections = new Reflections("mc.skyblock.plugin.listener");
        listenerReflections.getSubTypesOf(Listener.class).forEach(listener -> {
            try {
                Bukkit.getPluginManager().registerEvents(listener.getDeclaredConstructor().newInstance(), this);
                getLogger().info("Registered listener: " + listener.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

//        Bukkit.getScheduler().runTaskTimer(this, () -> {
//            if (Bukkit.getWorld("CYTOOX") == null) return;
//            Objects.requireNonNull(Bukkit.getWorld("CYTOOX")).getPlayers().forEach(player -> {
//                CustomSound.BACKGROUND_ECLIPSE.playSound(player, 0.1F, 1.0F, this.locationManager.getPosition("spawn").getLocation());
//            });
//        }, 0L, 20*30L);

    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getOnlinePlayers().forEach(player -> player.kick(Component.text("Server is restarting"))); //TODO: Change message
            this.islandManager.saveAll();
            this.warpManager.saveAll();
            this.userManager.saveAll();
            this.shopManager.saveAll();
            this.tagManager.deleteExistingTags();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
