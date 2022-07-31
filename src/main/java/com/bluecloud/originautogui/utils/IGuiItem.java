package com.bluecloud.originautogui.utils;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGuiItem {

    @NotNull ItemStack getItemStack();

    @NotNull String type();

    @Nullable String displayName();

    @Nullable List<String> lore();

    @NotNull List<String> enchantments();

}
