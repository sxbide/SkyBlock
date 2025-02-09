package eu.revanox.skyblock;

import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import eu.revanox.skyblock.codec.LocationCodec;
import eu.revanox.skyblock.command.*;
import eu.revanox.skyblock.inventory.IslandInventory;
import eu.revanox.skyblock.inventory.seller.SellerInventory;
import eu.revanox.skyblock.inventory.seller.SellerListInventory;
import eu.revanox.skyblock.island.IslandManager;
import eu.revanox.skyblock.listener.PlayerJoinListener;
import eu.revanox.skyblock.listener.PlayerQuitListener;
import eu.revanox.skyblock.location.LocationManager;
import eu.revanox.skyblock.scoreboard.ScoreboardManager;
import eu.revanox.skyblock.user.UserManager;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@FieldDefaults(makeFinal = true)
public class SkyBlockPlugin extends JavaPlugin {

    private static SkyBlockPlugin instance;

    @NonFinal
    private MongoManager mongoManager;
    @NonFinal
    private UserManager userManager;
    @NonFinal
    private ScoreboardManager scoreboardManager;
    @NonFinal
    private IslandManager islandManager;
    @NonFinal
    private LocationManager locationManager;

    @NonFinal
    private IslandInventory islandInventory;
    @NonFinal
    private SellerInventory sellerInventory;
    @NonFinal
    private SellerListInventory sellerListInventory;

    public static SkyBlockPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://revanox:Modd7V5hg2wAFYa30BvqMAk6ofVCCkRx@77.90.60.134:27017/", "skyblock"))
                .registerCodec(new LocationCodec());
        this.userManager = new UserManager();
        this.scoreboardManager = new ScoreboardManager(this);
        this.islandManager = new IslandManager();
        this.locationManager = new LocationManager();

        this.islandInventory = new IslandInventory();
        this.sellerInventory = new SellerInventory();
        this.sellerListInventory = new SellerListInventory();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.getCommand("island").setExecutor(new IslandCommand());
        this.getCommand("setlocation").setExecutor(new LocationCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("visit").setExecutor(new VisitCommand());
        this.getCommand("seller").setExecutor(new SellerCommand());

    }

    @Override
    public void onDisable() {
        try {
            this.islandManager.saveAll();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
