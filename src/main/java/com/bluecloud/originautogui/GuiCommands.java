package com.bluecloud.originautogui;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.bluecloud.originautogui.deluxemenu.items.DeluxeMenusFactory;
import com.bluecloud.originautogui.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;

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
                           @Optional @Nullable String flags) {

        Block targetBlock = player.getTargetBlock(6);

        path = path.replace(".yml", "");
        if (flags == null) flags = "";
        if (title == null) title = path;

        title = title.replace("_", " ");

        boolean overwrite = flags.contains("--overwrite");
        boolean open = flags.contains("--open");
        boolean updateConfig = flags.contains("--update-config") || flags.contains("--enable-now");
        boolean reloadPlugin = flags.contains("--reload-plugin") || flags.contains("--enable-now");

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

        if (updateConfig) {
            Plugin deluxeMenus = Bukkit.getPluginManager().getPlugin("DeluxeMenus");
            if (deluxeMenus != null) {
                ConfigurationSection fileSection = Objects.requireNonNull(deluxeMenus.getConfig().getConfigurationSection("gui_menus"));
                ConfigurationSection guiSection = fileSection.createSection(title.replace(" ", "_"));
                guiSection.set("file", path + ".yml");
                deluxeMenus.saveConfig();
            }
        }
        if (reloadPlugin) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dm reload");
        }
        if (open) {
            if (reloadPlugin && updateConfig) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dm open " + title + " " + player.getName());
            } else {
                player.sendMessage("Puoi usare questa flag solamente nel caso sia presente '--enable-now' oppure entrambe le flag '--update-config' e '--reload-plugin'");
            }
        }
    }

}
