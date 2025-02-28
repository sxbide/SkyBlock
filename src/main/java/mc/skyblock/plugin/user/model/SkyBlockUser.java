package mc.skyblock.plugin.user.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.tag.model.Tags;
import mc.skyblock.plugin.user.model.setting.Setting;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkyBlockUser {

    @Id
    UUID uniqueId;

    double balance;
    long goldPieces;

    Map<Tags, Boolean> tags;
    Tags selectedTag;

    List<String> enderChests;

    List<String> boughtWarps;

    Map<Setting, Integer> settings;

    public void setSelectedTag(Tags tag) {
        this.selectedTag = tag;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void addTag(Tags tag) {
        this.tags.put(tag, true);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void removeTag(Tags tag) {
        this.tags.remove(tag);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public boolean hasTag(Tags tag) {
        boolean hasTag = this.tags.computeIfAbsent(tag, k -> false);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
        return hasTag;
    }

    public void setBalance(double amount) {
        this.balance = amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void addBalance(double amount) {
        this.balance += amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void removeBalance(double amount) {
        this.balance -= amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void setGoldPieces(long amount) {
        this.goldPieces = amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void addGoldPieces(long amount) {
        this.goldPieces += amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void removeGoldPieces(long amount) {
        this.goldPieces -= amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void buyWarp(String warpName) {
        this.boughtWarps.add(warpName);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public void removeWarp(String warpName) {
        this.boughtWarps.remove(warpName);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public boolean hasWarp(String warpName) {
        return this.boughtWarps.contains(warpName);
    }

    public void setSetting(Setting setting, int value) {
        this.settings.put(setting, value);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

    public int getSetting(Setting setting) {
        return this.settings.computeIfAbsent(setting, Setting::getDefaultValue);
    }

    public void toggleSetting(Setting setting) {
        int currentValue = this.settings.computeIfAbsent(setting, Setting::getDefaultValue);
        int nextValue = (currentValue + 1) % setting.getValues().size();
        this.settings.put(setting, nextValue);
        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
    }

//    public void sendPrivateMessage(UUID sender, String message) {
//        LocalDateTime timestamp = LocalDateTime.now();
//        boolean isOnline = SkyBlockPlugin.instance().getServer().getPlayer(uniqueId) != null;
//        PrivateMessage privateMessage = new PrivateMessage(timestamp, sender, uniqueId, message, !isOnline, false);
//        if (isOnline) {
//            Player player = SkyBlockPlugin.instance().getServer().getPlayer(uniqueId);
//            if (player == null) {
//                return;
//            }
//            String miniMessageString = "<#00ffff>⚑ <gold>DM von <#00ffff>" + SkyBlockPlugin.instance().getServer().getOfflinePlayer(sender).getName() + "<gold>: <#00ffff>" + message;
//            player.sendMessage(MiniMessage.miniMessage().deserialize(miniMessageString));
//            privateMessage.setRead(true);
//        } else {
//            privateMessage.setRead(false);
//        }
//        this.privateMessageLog.addEntry(timestamp, privateMessage);
//        SkyBlockPlugin.instance().getUserManager().saveUser(uniqueId, this);
//    }

}
