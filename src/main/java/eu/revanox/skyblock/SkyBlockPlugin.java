package eu.revanox.skyblock;

import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import eu.revanox.skyblock.auctions.AuctionsManager;
import eu.revanox.skyblock.codec.ItemStackCodec;
import eu.revanox.skyblock.codec.LocationCodec;
import eu.revanox.skyblock.command.*;
import eu.revanox.skyblock.island.IslandManager;
import eu.revanox.skyblock.listener.*;
import eu.revanox.skyblock.location.LocationManager;
import eu.revanox.skyblock.scoreboard.ScoreboardManager;
import eu.revanox.skyblock.tablist.TablistManager;
import eu.revanox.skyblock.user.UserManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
    private AuctionsManager auctionsManager;
    @NonFinal
    private LuckPerms luckPerms;
    @NonFinal
    private InventoryManager inventoryManager;
    @NonFinal
    private TablistManager tablistManager;


    public static SkyBlockPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.luckPerms = LuckPermsProvider.get();

        this.mongoManager = new MongoManager(Credentials.of("mongodb://revanox:Modd7V5hg2wAFYa30BvqMAk6ofVCCkRx@77.90.60.134:27017/", "skyblock"))
                .registerCodec(new LocationCodec())
                .registerCodec(new ItemStackCodec());
        this.userManager = new UserManager();
        this.scoreboardManager = new ScoreboardManager(this);
        this.islandManager = new IslandManager();
        this.locationManager = new LocationManager();
        this.auctionsManager = new AuctionsManager();
        this.tablistManager = new TablistManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.invoke();


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);

        this.getCommand("island").setExecutor(new IslandCommand());
        this.getCommand("setlocation").setExecutor(new LocationCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("visit").setExecutor(new VisitCommand());
        this.getCommand("seller").setExecutor(new SellerCommand());
        this.getCommand("gold").setExecutor(new GoldPiecesCommand());
        this.getCommand("auctions").setExecutor(new AuctionsCommand());
        this.getCommand("perk").setExecutor(new PerkCommand());

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
