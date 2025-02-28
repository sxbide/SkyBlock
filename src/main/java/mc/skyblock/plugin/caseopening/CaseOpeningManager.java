package mc.skyblock.plugin.caseopening;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.caseopening.animation.CaseOpeningAnimation;
import mc.skyblock.plugin.caseopening.mongo.model.Case;
import mc.skyblock.plugin.caseopening.mongo.model.item.CaseItem;
import mc.skyblock.plugin.caseopening.mongo.repository.CaseRepository;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.ItemBuilder;
import mc.skyblock.plugin.util.Rarity;
import mc.skyblock.plugin.util.SoundAction;
import mc.skyblock.plugin.util.custom.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseOpeningManager {

    CaseRepository repository;

    @Setter
    @NonFinal
    Case aCase;
    @NonFinal
    boolean opening;

    public CaseOpeningManager() {
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(CaseRepository.class);
        this.aCase = this.repository.countAll() == 0 ? null : this.repository.findAll().getFirst();
        if (this.aCase == null) {
            this.aCase = new Case();
            this.aCase.setCaseBlockMaterial(Material.DROPPER);
            this.aCase.setKeyItem(CustomItems.ANTIQUE_KEY.itemStack());
            this.aCase.setItems(new ArrayList<>());
            this.repository.save(this.aCase);
        }
    }

    public ItemStack getCaseKeyItem() {
        return this.aCase.getKeyItem();
    }

    public List<CaseItem> getCaseItems() {
        return this.aCase.getItems();
    }

    public void open(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!getCaseKeyItem().isSimilar(itemInHand)) {
            player.sendMessage(ChatAction.failure("Du benötigst einen Schlüssel, um die Kiste zu öffnen."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (opening) {
            player.sendMessage(ChatAction.failure("Es wird bereits eine Kiste geöffnet."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (this.aCase.getItems().isEmpty()) {
            player.sendMessage(ChatAction.failure("Es sind keine Items in der Kiste."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (itemInHand.getAmount() > 1) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(ItemStack.empty());
        }
        opening = true;
        new CaseOpeningAnimation(player).start(() -> opening = false);
    }

    public CaseItem getCaseItem(ItemStack itemStack) {
        for (CaseItem caseItem : getCaseItems()) {
            if (caseItem.getItemStack().equals(itemStack)) {
                return caseItem;
            }
        }
        return null;
    }

    public CaseItem getRandomCaseItem() {
        if (getCaseItems() == null || getCaseItems().isEmpty()) {
            throw new IllegalArgumentException("The case items list cannot be empty.");
        }

        // Calculate the total sum of all chances
        double totalChance = getCaseItems().stream().mapToDouble(CaseItem::getChance).sum();

        // Generate a random number between 0 and totalChance
        double randomValue = new Random().nextDouble() * totalChance;

        // Iterate over the list and select an item based on the random value
        double cumulativeChance = 0.0;
        for (CaseItem caseItem : getCaseItems()) {
            cumulativeChance += caseItem.getChance();
            if (randomValue <= cumulativeChance) {
                return caseItem;
            }
        }

        // Fallback (should never happen if chances are set correctly)
        return getCaseItems().getLast();
    }


}
