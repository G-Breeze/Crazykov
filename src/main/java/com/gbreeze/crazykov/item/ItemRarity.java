package com.gbreeze.crazykov.item;

import net.minecraft.util.Formatting;

public enum ItemRarity {
    COMMON(0xFFFFFFFF, Formatting.WHITE),     // 普通 - 白色
    FINE(0xFF55FF55, Formatting.GREEN),       // 精良 - 绿色
    UNCOMMON(0xFF5555FF, Formatting.BLUE),    // 稀有 - 蓝色
    RARE(0xFFAA00AA, Formatting.DARK_PURPLE), // 珍贵 - 紫色
    EPIC(0xFFFFAA00, Formatting.GOLD),        // 珍藏 - 金色
    LEGENDARY(0xFFFF5555, Formatting.RED);    // 珍宝 - 红色

    public final int color;
    public final Formatting formatting;

    ItemRarity(int color, Formatting formatting) {
        this.color = color;
        this.formatting = formatting;
    }
}
