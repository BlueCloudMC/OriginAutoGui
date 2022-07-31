package com.bluecloud.originautogui.deluxemenu.items;

import com.bluecloud.originautogui.utils.GuiFactory;
import com.bluecloud.originautogui.utils.GuiItem;
import com.bluecloud.originautogui.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeluxeMenusFactory implements GuiFactory, AutoCloseable {

    private final File file;
    private final FileConfiguration configuration;

    public DeluxeMenusFactory(@NotNull File file, @NotNull String title, boolean overwrite) throws IOException {
        this.file = file;
        FileConfiguration dmConfiguration = createConfiguration(file, overwrite);
        if (dmConfiguration == null) {
            throw new FileAlreadyExistsException("File: " + file.getPath() + "already exist");
        }
        this.configuration = dmConfiguration;

        configuration.set("menu_title", title);
        configuration.set("open_commands", new ArrayList<>());
    }

    @Override
    public void serializeContent(@NotNull Inventory content) {
        int guiSize = getGuiSize(content);
        configuration.set("size", guiSize);
        ConfigurationSection itemsSection = configuration.createSection("items");
        List<GuiItem> items = scanContent(content);
        int count = 0;
        for (GuiItem item : items) {
            ConfigurationSection itemSection = itemsSection.createSection(String.valueOf(count));
            item.serialize(itemSection);
            count++;
        }
    }

    private @NotNull List<GuiItem> scanContent(@NotNull Inventory content) {
        List<GuiItem> result = new ArrayList<>();
        Set<ItemStack> uniqueItems = new HashSet<>();

        for (ItemStack itemStack : content) {
            uniqueItems.add(itemStack);
        }

        for (ItemStack uniqueItem : uniqueItems) {
            if (uniqueItem != null && uniqueItem.getType() != Material.AIR) {
                List<Integer> slots = InventoryUtils.getSlots(uniqueItem, content);
                if (slots.size() > 1) {
                    if (slots.size() != 9 && uniqueItem.getType() != Material.BARRIER) {
                        FillerItem filler = new FillerItem(uniqueItem.clone(), slots);
                        result.add(filler);
                    }
                } else {
                    int slot = slots.get(0);
                    if (uniqueItem.getType() == Material.PLAYER_HEAD) {
                        HeadItem headItem = new HeadItem(uniqueItem, slot);
                        result.add(headItem);
                    } else {
                        StandardItem standardItem = new StandardItem(uniqueItem, slot);
                        result.add(standardItem);
                    }
                }
            }
        }
        return normalize(result, content);
    }


    private List<GuiItem> normalize(@NotNull List<GuiItem> items, @NotNull Inventory content) {
        for (GuiItem item : items) {
            item.normalize(getNormalizationFactor(content));
        }
        return items;
    }

    private int getNormalizationFactor(Inventory content) {
        int firstItemSlot = getFirstItemSlot(content);
        List<Integer> skippedRows = getSkippedRows(content);
        int factor = 0;
        for (Integer skippedRow : skippedRows) {
            int lastSkippedSlot = skippedRow * 9;
            if (firstItemSlot > lastSkippedSlot) {
                factor++;
            }
        }
        return factor;
    }

    private List<Integer> getSkippedRows(Inventory content) {
        List<Integer> skippedRows = new ArrayList<>();
        int rows = content.getSize() / 9;
        for (int i = 0; i < rows; i++) {
            int rowStart = i * 9;
            int rowEnd = rowStart + 9;
            int count = InventoryUtils.count(content, new ItemStack(Material.BARRIER), rowStart, rowEnd);
            if (count == 9) {
                skippedRows.add(i);
            }
        }
        return skippedRows;
    }

    private int getFirstItemSlot(Inventory content) {
        for (int i = 0; i < content.getSize(); i++) {
            ItemStack itemStack = content.getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType() != Material.BARRIER) {
                return i;
            }
        }
        return -1;
    }

    private int getGuiSize(Inventory content) {
        return ((content.getSize() / 9) - getSkippedRows(content).size()) * 9;
    }


    private @Nullable FileConfiguration createConfiguration(File file, boolean overwrite) throws IOException {
        if (!file.exists() || overwrite) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } else {
            return null;
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @NotNull FileConfiguration getConfig() {
        return configuration;
    }

    @Override
    public void close() throws IOException {
        configuration.save(file);
    }
}
