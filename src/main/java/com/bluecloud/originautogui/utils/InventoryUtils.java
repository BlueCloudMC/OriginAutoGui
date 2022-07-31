package com.bluecloud.originautogui.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class InventoryUtils {

    private InventoryUtils() {
    }

    public static @NotNull List<Integer> getSlots(@NotNull ItemStack itemStack, @NotNull Inventory inventory) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (itemStack.equals(item)) {
                slots.add(i);
            }
        }
        return slots;
    }


    public static int count(@NotNull Inventory inventory, @NotNull ItemStack itemStack, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (itemStack.equals(inventory.getItem(i))) {
                count++;
            }
        }
        return count;
    }

}
