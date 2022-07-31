package com.bluecloud.originautogui.deluxemenu.items;

import com.bluecloud.originautogui.OriginAutoGui;
import com.bluecloud.originautogui.utils.GuiItem;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HeadItem extends GuiItem {

    private final ItemStack itemStack;

    public HeadItem(@NotNull ItemStack itemStack, int slot) {
        super(itemStack, new Integer[]{slot});
        if (itemStack.getType() != Material.PLAYER_HEAD) {
            throw new IllegalStateException("ItemStack must be player head");
        }
        this.itemStack = itemStack;
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NotNull String type() {
        HeadDatabaseAPI api = OriginAutoGui.getHeadDatabaseApi();
        if (api != null) {
            String id = api.getItemID(itemStack);
            if (id != null) {
                return "hdb-" + id;
            }
        }
        return "BARRIER";
    }


}
