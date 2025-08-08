package com.gbreeze.crazykov.effects;

import com.gbreeze.crazykov.CrazyKov;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect ARM_FRACTURE_DEBUFF = new DebuffEffects.ArmFractureEffect();
    public static final StatusEffect LEG_FRACTURE_DEBUFF = new DebuffEffects.LegFractureEffect();
    public static final StatusEffect BLEEDING_DEBUFF = new DebuffEffects.BleedingEffect();
    public static final StatusEffect HEADACHE_DEBUFF = new DebuffEffects.HeadacheEffect();
    public static final StatusEffect BACKACHE_DEBUFF = new DebuffEffects.BackacheEffect();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(CrazyKov.MOD_ID, "arm_fracture"), ARM_FRACTURE_DEBUFF);
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(CrazyKov.MOD_ID, "leg_fracture"), LEG_FRACTURE_DEBUFF);
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(CrazyKov.MOD_ID, "bleeding"), BLEEDING_DEBUFF);
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(CrazyKov.MOD_ID, "head_ache"), HEADACHE_DEBUFF);
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(CrazyKov.MOD_ID, "back_ache"), BACKACHE_DEBUFF);
    }
}
