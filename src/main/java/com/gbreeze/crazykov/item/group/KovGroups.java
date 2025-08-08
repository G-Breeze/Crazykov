package com.gbreeze.crazykov.item.group;

import com.gbreeze.crazykov.CrazyKov;
import com.gbreeze.crazykov.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KovGroups {
    // 自定义物品组的注册键
    public static final RegistryKey<ItemGroup> MEDICAL_GROUP = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(CrazyKov.MOD_ID, "medical_group")
    );
    public static final RegistryKey<ItemGroup> WEAPON_GROUP = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(CrazyKov.MOD_ID, "weapon_group")
    );
    public static final RegistryKey<ItemGroup> ARMOR_GROUP = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(CrazyKov.MOD_ID, "armor_group")
    );

    public static final ItemGroup MEDICAL_ITEM_GROUP = ItemGroup.create(ItemGroup.Row.TOP, 0).
            displayName(Text.translatable("itemGroup." + CrazyKov.MOD_ID + ".medical_group"))
            .icon(() -> new ItemStack(ModItems.MEDICAL_KIT))
            .build();

    public static final ItemGroup WEAPON_ITEM_GROUP = ItemGroup.create(ItemGroup.Row.TOP, 1)
            .displayName(Text.translatable("itemGroup." + CrazyKov.MOD_ID + ".weapon_group"))
            .build();

    public static final ItemGroup ARMOR_ITEM_GROUP = ItemGroup.create(ItemGroup.Row.TOP, 2)
            .displayName(Text.translatable("itemGroup." + CrazyKov.MOD_ID + ".armor_group"))
            .build();

    public static void register() {
        registerItemGroup(MEDICAL_GROUP, MEDICAL_ITEM_GROUP);
        registerItemGroup(WEAPON_GROUP, WEAPON_ITEM_GROUP);
        registerItemGroup(ARMOR_GROUP, ARMOR_ITEM_GROUP);
    }

    private static void registerItemGroup(RegistryKey<ItemGroup> key, ItemGroup group) {
        Registry.register(Registries.ITEM_GROUP, key, group);
    }
}
