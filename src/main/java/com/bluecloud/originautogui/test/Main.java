package com.bluecloud.originautogui.test;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern MINECRAFT_HEX_PATTERN = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    public static final char COLOR_CHAR = '\u00A7';
    public static void main(String[] args) {
        System.out.println(COLOR_CHAR);
    }

    public static String translateMinecraftCodes(@NotNull String text) {
        Matcher matcher = MINECRAFT_HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String translated = group
                    .replace("§x", "&#")
                    .replace("§", "");
            text = text.replace(group, translated);
        }
        return text;
    }

}
