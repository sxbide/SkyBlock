package eu.revanox.skyblock.util;

import eu.revanox.skyblock.tag.model.Tags;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class Voucher {

    public ItemStack getBalanceVoucher(double value) {
        return ItemBuilder.of(Material.PAPER)
                .displayName(Component.text("§b☆ §3Gutschein §b☆"))
                .lore(
                        Component.empty(),
                        Component.text("§7Wert: §b" + NumberUtil.formatBalance(value) + " ⛃"),
                        Component.empty(),
                        Component.text("§7Rechtsklick um den Voucher einzulösen")
                ).unbreakable().build();
    }

    public ItemStack getTagVoucher(Tags tag) {
        return ItemBuilder.of(Material.NAME_TAG)
                .displayName(Component.text("§b☆ §3Titel §b☆"))
                .lore(
                        Component.empty(),
                        Component.text("§7Titel: §r").append(tag.getTagText()),
                        Component.text("§7Rarität: §r").append(tag.getRarity().getDisplayName()),
                        Component.empty(),
                        Component.text("§7Rechtsklick um den Titel einzulösen")
                ).unbreakable().build();
    }

    public boolean isTagVoucher(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        var meta = itemStack.getItemMeta();
        if (!meta.hasDisplayName() || !meta.displayName().equals(Component.text("§b☆ §3Titel §b☆"))) return false;
        var lore = meta.lore();
        if (lore == null || lore.size() != 5) return false;
        if (!lore.get(0).equals(Component.empty()) || !lore.get(3).equals(Component.text("§7Rechtsklick um den Titel einzulösen")))
            return false;
        var plainText = PlainTextComponentSerializer.plainText();
        return plainText.serialize(lore.get(1)).startsWith("§7Titel: §r") && plainText.serialize(lore.get(2)).startsWith("§7Rarität: §r");
    }

    public boolean isBalanceVoucher(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        var meta = itemStack.getItemMeta();
        if (!meta.hasDisplayName() || !meta.displayName().equals(Component.text("§b☆ §3Gutschein §b☆"))) return false;
        var lore = meta.lore();
        if (lore == null || lore.size() != 4) return false;
        if (!lore.get(0).equals(Component.empty()) || !lore.get(2).equals(Component.empty()) || !lore.get(3).equals(Component.text("§7Rechtsklick um den Voucher einzulösen")))
            return false;
        var plainText = PlainTextComponentSerializer.plainText();
        var loreContent = plainText.serialize(lore.get(1));
        return loreContent.startsWith("§7Wert: §b") && loreContent.endsWith(" ⛃");
    }

    public double getBalanceVoucherValue(ItemStack itemStack) {
        if (!isBalanceVoucher(itemStack)) return 0;
        var loreContent = PlainTextComponentSerializer.plainText().serialize(itemStack.getItemMeta().lore().get(1));
        return Double.parseDouble(loreContent.substring(8, loreContent.length() - 2));
    }

    public Tags getTagVoucherValue(ItemStack itemStack) {
        if (!isTagVoucher(itemStack)) return null;
        var loreContent = PlainTextComponentSerializer.plainText().serialize(itemStack.getItemMeta().lore().get(1));
        return Tags.getByDisplayName(loreContent.substring(7));
    }
}