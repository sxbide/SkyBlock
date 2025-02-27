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
import mc.skyblock.plugin.util.Rarity;
import mc.skyblock.plugin.util.SoundAction;
import mc.skyblock.plugin.util.custom.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseOpeningManager {

    CaseRepository repository;
    ItemStack caseKeyItem;
    List<CaseItem> caseItems;

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
        caseKeyItem = aCase.getKeyItem();
        caseItems = new ArrayList<>(aCase.getItems());
    }

    public void open(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!caseKeyItem.isSimilar(itemInHand)) {
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
        for (CaseItem caseItem : caseItems) {
            if (caseItem.getItemStack().equals(itemStack)) {
                return caseItem;
            }
        }
        return null;
    }

    public CaseItem getRandomCaseItem() {
        return new ArrayList<>(caseItems).get((int) (Math.random() * caseItems.size()));
    }

}
