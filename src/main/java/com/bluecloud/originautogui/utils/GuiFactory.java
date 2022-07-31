package com.bluecloud.originautogui.utils;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface GuiFactory {

    void serializeContent(@NotNull Inventory content);

}
