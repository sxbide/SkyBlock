package mc.skyblock.plugin;

import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import mc.skyblock.plugin.auctions.AuctionsManager;
import mc.skyblock.plugin.codec.ItemStackCodec;
import mc.skyblock.plugin.codec.LocationCodec;
import mc.skyblock.plugin.command.CommandManager;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.impl.ResourcePackConfiguration;
import mc.skyblock.plugin.guild.GuildManager;
import mc.skyblock.plugin.island.IslandManager;
import mc.skyblock.plugin.listener.*;
import mc.skyblock.plugin.location.LocationManager;
import mc.skyblock.plugin.npc.NPCManager;
import mc.skyblock.plugin.scoreboard.ScoreboardManager;
import mc.skyblock.plugin.tablist.TablistManager;
import mc.skyblock.plugin.tag.TagManager;
import mc.skyblock.plugin.user.UserManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SkyBlockPlugin extends JavaPlugin {

    private static SkyBlockPlugin instance;

    private ResourcePackConfiguration resourcePackConfiguration;

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
    private GuildManager guildManager;
    private NPCManager npcManager;


    public static SkyBlockPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.resourcePackConfiguration = Configuration.load(getDataFolder().toPath().resolve("resourcepack.json").toString(), ResourcePackConfiguration.class);

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
        this.guildManager = new GuildManager();
        this.npcManager = new NPCManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.invoke();


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerResourcePackListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAfkListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerNpcInteractListener(), this);

    }

    @Override
    public void onDisable() {
        try {
            this.islandManager.saveAll();
            this.userManager.saveAll();
            this.tagManager.deleteExistingTags();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
