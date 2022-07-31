package com.bluecloud.originautogui.utils.hbd;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public class HeadDatabaseManager implements Listener {

    private HeadDatabaseAPI api;

    @EventHandler
    public void on(DatabaseLoadEvent event) {
        this.api = new HeadDatabaseAPI();
    }

    public @Nullable HeadDatabaseAPI getApi() {
        if (api == null) {
            api = new HeadDatabaseAPI();
        }
        return api;
    }

}
