package com.gbreeze.crazykov.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModularItem extends Item {
    private final ItemRarity rarity;
    private final float weight;
    private final Integer maxCount;
    private ItemLayout layout;

    public ModularItem(Settings settings, ItemRarity rarity, float weight, Integer maxCount) {
        super(settings
                .maxCount(maxCount));
        this.rarity = rarity;
        this.weight = weight;
        this.maxCount = maxCount;
        this.layout = createDefaultLayout();
    }

    protected ItemLayout createDefaultLayout() {
        return new ItemLayout(1, 1, new boolean[1][1]);
    }

    public ItemLayout getLayout() {
        return layout;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public float getWeight() {
        return weight;
    }

    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().formatted(rarity.formatting);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.crazykov.weight", weight).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.crazykov.rarity." + rarity.name().toLowerCase())
                .formatted(rarity.formatting));
    }
}
