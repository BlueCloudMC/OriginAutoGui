package com.bluecloud.originautogui;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.bluecloud.originautogui.deluxemenu.items.DeluxeMenusFactory;
import com.bluecloud.originautogui.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@CommandAlias("autogui")
public class GuiCommands extends BaseCommand {

    private final OriginAutoGui plugin;

    public GuiCommands(OriginAutoGui plugin) {
        this.plugin = plugin;
    }

    @Subcommand("test")
    public void onTest(Player player) {
        String displayName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        System.out.println(ColorUtils.translateMinecraftCodes(displayName));
    }

    @Subcommand("create dm")
    @CommandCompletion("<path> [title] @flags")
    public void onDmCreate(@NotNull Player player,
                           @Single @NotNull String path,
                           @Optional @Single @Nullable String title,
                           @Optional @Nullable String flags) throws IOException {

        Block targetBlock = player.getTargetBlock(6);

        path = path.replace(".yml", "");
        if (flags == null) flags = "";
        if (title == null) title = path;

        title = title.replace("_", " ");

        boolean overwrite = flags.contains("--overwrite");

        File file = new File(plugin.getDeluxeMenusPath(), path + ".yml");

        try (DeluxeMenusFactory deluxeMenusFactory = new DeluxeMenusFactory(file, title, overwrite)) {
            if (targetBlock.getState() instanceof Chest chest) {

                deluxeMenusFactory.serializeContent(chest.getInventory());
            }
            player.sendMessage("Gui salvata in: " + file.getPath());
        } catch (FileAlreadyExistsException e) {
            player.sendMessage("Un file con questo nome è già esistente");
        } catch (IOException e) {
            player.sendMessage("Qualcosa è andato terribilmente storto, ops");
        }

    }

}
