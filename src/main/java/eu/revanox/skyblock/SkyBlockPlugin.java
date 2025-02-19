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
import eu.revanox.skyblock.tag.TagManager;
import eu.revanox.skyblock.user.UserManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SkyBlockPlugin extends JavaPlugin {

    private static SkyBlockPlugin instance;

    private MongoManager mongoManager;
    private UserManager userManager;
    private ScoreboardManager scoreboardManager;
    private IslandManager islandManager;
    private LocationManager locationManager;
    private AuctionsManager auctionsManager;
    private LuckPerms luckPerms;
    private InventoryManager inventoryManager;
    private TablistManager tablistManager;
    private CommandManager commandManager;
    private TagManager tagManager;


    public static SkyBlockPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.luckPerms = LuckPermsProvider.get();

        this.mongoManager = new MongoManager(Credentials.of("mongodb://keinepixel:rc8Saw8UNU4Dp+8UuWOUuVWZnJOEwp2nYhOcvqf5L@87.106.178.7:27017/", "skyblock"))
                .registerCodec(new LocationCodec())
                .registerCodec(new ItemStackCodec());
        this.userManager = new UserManager();
        this.scoreboardManager = new ScoreboardManager(this);
        this.islandManager = new IslandManager();
        this.locationManager = new LocationManager();
        this.auctionsManager = new AuctionsManager();
        this.tablistManager = new TablistManager();
        this.commandManager = new CommandManager(this);
        this.tagManager = new TagManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.invoke();


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);

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
