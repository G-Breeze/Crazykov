package com.gbreeze.crazykov.item;

import com.gbreeze.crazykov.CrazyKov;
import com.gbreeze.crazykov.item.group.KovGroups;
import com.gbreeze.crazykov.item.medical.BandageItem;
import com.gbreeze.crazykov.item.medical.MedicalKitItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {

    // 模组物品
    public static final ModularItem MEDICAL_KIT = new MedicalKitItem(new Settings(), ItemRarity.UNCOMMON, 1.5f, 80, 40, 10, 2);
    public static final ModularItem BANDAGE = new BandageItem(new Settings(), ItemRarity.UNCOMMON, 0.5f, 8, 40, 50);
    public static final ModularItem AMMO_BOX = new ModularItem(new Settings(), ItemRarity.RARE, 2.0f, 8);

    public static void register() {
        // 注册所有物品
        Registry.register(Registries.ITEM, Identifier.of(CrazyKov.MOD_ID, "medical_kit"), MEDICAL_KIT);
        Registry.register(Registries.ITEM, Identifier.of(CrazyKov.MOD_ID, "bandage"), BANDAGE);
        Registry.register(Registries.ITEM, Identifier.of(CrazyKov.MOD_ID, "ammo_box"), AMMO_BOX);

        ItemGroupEvents.modifyEntriesEvent(KovGroups.MEDICAL_GROUP)
                .register(entries -> {
                    entries.add(MEDICAL_KIT);
                    entries.add(BANDAGE);
                });
        ItemGroupEvents.modifyEntriesEvent(KovGroups.WEAPON_GROUP)
                .register(entries -> {
                    entries.add(AMMO_BOX);
                });
    }
}
