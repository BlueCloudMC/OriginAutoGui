package com.bluecloud.originautogui.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public final class ColorUtils {

    private ColorUtils() {
    }

    private static final Pattern MINECRAFT_HEX_PATTERN = Pattern.compile(COLOR_CHAR + "x(" + COLOR_CHAR + "[0-9a-fA-F]){6}");

    public static List<String> translateMinecraftCodes(@NotNull List<String> lore) {
        List<String> result = new ArrayList<>();
        for (String s : lore) {
            result.add(translateMinecraftCodes(s));
        }
        return result;
    }

    public static String translateMinecraftCodes(@NotNull String text) {
        Matcher matcher = MINECRAFT_HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String translated = group
                    .replace(COLOR_CHAR + "x", "&#")
                    .replace(String.valueOf(COLOR_CHAR), "");

            text = text.replace(group, translated);
        }

        return text.replace(String.valueOf(COLOR_CHAR), "&");
    }

}
