package com.bluecloud.originautogui.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GuiItem implements IGuiItem, ItemSerializer {

    private Integer[] slot;
    private final ItemStack itemStack;

    protected GuiItem(ItemStack itemStack, Integer[] slot) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    private void setSlots(Integer[] slots) {
        this.slot = slots;
    }

    public final void normalize(int factor) {
        if (factor == 0) return;

        Integer[] newSlots = new Integer[this.slot.length];
        for (int i = 0; i < getSlots().length; i++) {
            int currentSlot = getSlots()[i];
            newSlots[i] = currentSlot - (9 * factor);
        }
        setSlots(newSlots);
    }


    public final Integer[] getSlots() {
        return slot;
    }


    @Override
    public final void serialize(ConfigurationSection section) {
        section.set("material", type());
        if (getSlots().length == 1) {
            section.set("slot", getSlots()[0]);
        } else {
            section.set("slots", getSlots());
        }
        String str = ColorUtils.translateMinecraftCodes(displayName());
        section.set("display_name", str);
        section.set("lore", lore());
        section.set("enchantments", enchantments());
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NotNull String type() {
        return itemStack.getType().name();
    }

    @Override
    public @Nullable String displayName() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            return meta.getDisplayName();
        }
        return null;
    }

    @Override
    public @Nullable List<String> lore() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) lore = new ArrayList<>();
            return ColorUtils.translateMinecraftCodes(lore);
        }
        return null;
    }

    @Override
    public @NotNull List<String> enchantments() {
        Map<Enchantment, Integer> enchants = itemStack.getEnchantments();
        List<String> result = new ArrayList<>();
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            String serializedEnchant = entry.getKey().getKey().value() + ":" + entry.getValue();
            result.add(serializedEnchant);
        }
        return result;
    }
}
