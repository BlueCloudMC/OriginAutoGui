package com.bluecloud.originautogui;

import co.aikar.commands.PaperCommandManager;
import com.bluecloud.originautogui.utils.hbd.HeadDatabaseManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public final class OriginAutoGui extends JavaPlugin {

    private static final HeadDatabaseManager headDatabaseManager;

    private PaperCommandManager commandManager;
    private String deluxeMenusPath;

    static {
        headDatabaseManager = new HeadDatabaseManager();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(headDatabaseManager, this);

        commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerCompletion("flags", ctx ->
                List.of("--overwrite", "--enable-now", "--update-config", "--reload-plugin", "--open"));

        commandManager.registerCommand(new GuiCommands(this));

        deluxeMenusPath = getConfig().getString("deluxe-menus.dir");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public @NotNull File getDeluxeMenusPath() {
        return new File(getDataFolder().getParent() + deluxeMenusPath);
    }

    public static @Nullable HeadDatabaseAPI getHeadDatabaseApi() {
        return headDatabaseManager.getApi();
    }

}
