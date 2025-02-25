package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.util.ResourceIcons;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SocialsCommand extends AbstractCommand {

    public SocialsCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "socials", null, "social", "discord", "twitter", "instagram", "youtube", "twitch", "website");
    }

    @Override
    public void run(Player player, String[] args) {
        Component pageOne = Component.newline()
                            .append(Component.text("§r" + ResourceIcons.SCOREBOARD_HEADER.unicode()))
                            .appendNewline()
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nDiscord§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserem Discord zu gelangen."))).clickEvent(ClickEvent.openUrl("https://discord.gg/blockarion")))
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nTwitter§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserem Twitter zu gelangen."))).clickEvent(ClickEvent.openUrl("https://twitter.com/blockarion")))
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nInstagram§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserem Instagram zu gelangen."))).clickEvent(ClickEvent.openUrl("https://instagram.com/blockarion")))
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nYouTube§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserem YouTube zu gelangen."))).clickEvent(ClickEvent.openUrl("https://youtube.com/blockarion")))
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nTwitch§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserem Twitch zu gelangen."))).clickEvent(ClickEvent.openUrl("https://twitch.tv/blockarion")))
                            .appendNewline()
                            .append(Component.text("§8→ §7§l§nWebsite§r").hoverEvent(HoverEvent.showText(Component.text("§7Klicke, um zu unserer Website zu gelangen."))).clickEvent(ClickEvent.openUrl("https://blockarion.eu")))
                            .appendNewline()
                            .appendNewline()
                            .append(Component.text("§7Klicke auf die Links, um zu den jeweiligen Socials zu gelangen."));

        Book book = Book.builder()
                .title(Component.text("Unsere Socials"))
                .author(Component.text("Blockarion"))
                .addPage(pageOne)
                .build();

        player.openBook(book);
    }
}
