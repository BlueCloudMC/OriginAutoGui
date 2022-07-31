package com.bluecloud.originautogui.deluxemenu.items;

import com.bluecloud.originautogui.utils.GuiItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StandardItem extends GuiItem {

    public StandardItem(@NotNull ItemStack itemStack, int slot) {
        super(itemStack, new Integer[]{slot});
    }

}
