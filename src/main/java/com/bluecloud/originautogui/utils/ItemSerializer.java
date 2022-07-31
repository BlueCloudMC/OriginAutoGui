package com.bluecloud.originautogui.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface ItemSerializer {

    void serialize(@NotNull ConfigurationSection section);

}
