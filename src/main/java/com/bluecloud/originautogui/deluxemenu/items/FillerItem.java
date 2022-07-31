package com.bluecloud.originautogui.deluxemenu.items;

import com.bluecloud.originautogui.utils.GuiItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FillerItem extends GuiItem {

    public FillerItem(ItemStack itemStack, List<Integer> slots) {
        super(itemStack, slots.toArray(new Integer[0]));
    }

}
