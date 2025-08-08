package com.gbreeze.crazykov.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.UUID;

public class DebuffEffects {
    // 唯一UUID用于属性修饰符
    private static final UUID ARM_FRACTURE_UUID = UUID.fromString("a3d5a8e1-1b9c-4f8d-b2e7-18a9b4c6d0f0");
    private static final UUID LEG_FRACTURE_UUID = UUID.fromString("b4e6b9f2-2c0d-5e9e-c3f8-29b0c5d7e1f0");
    private static final UUID BLEEDING_UUID = UUID.fromString("c5f7c0d3-3e1f-6e0f-d4e9-3ac1d6e8f2e1");
    private static final UUID HEADACHE_UUID = UUID.fromString("d6e8d1e4-4f2e-7e1e-e5e0-4bd2e7f9e3b2");
    private static final UUID BACKACHE_UUID = UUID.fromString("e6e2a1b2-1f1e-5a2d-e2f8-2ad9c6f2e2a3");

    // 手骨折效果
    public static class ArmFractureEffect extends StatusEffect {
        private PlayerEntity player;

        public ArmFractureEffect() {
            super(StatusEffectCategory.HARMFUL, 0x8B4513); // 棕色
        }

        @Override
        public void onApplied(LivingEntity entity, int amplifier) {
            if (entity instanceof PlayerEntity player) {
                this.player = player;
                EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
                attributeInstance.removeModifier(Identifier.of(ARM_FRACTURE_UUID.toString()));

                // 计算攻击速度减少量 (每级减少20%)
                double reduction = (amplifier + 1) * -0.20;
                attributeInstance.addPersistentModifier(new EntityAttributeModifier(
                        Identifier.of(ARM_FRACTURE_UUID.toString()),
                        reduction,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                );
            }
        }

        @Override
        public void onRemoved(AttributeContainer attributes) {
            if (this.player instanceof PlayerEntity) {
                EntityAttributeInstance speedAttribute = this.player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
                if (speedAttribute != null) {
                    speedAttribute.removeModifier(Identifier.of(ARM_FRACTURE_UUID.toString())); // 确保移除修饰符
                }
            }
        }
    }

    // 腿骨折效果
    public static class LegFractureEffect extends StatusEffect {
        private PlayerEntity player;

        public LegFractureEffect() {
            super(StatusEffectCategory.HARMFUL, 0x8B4513); // 棕色
        }

        @Override
        public void onApplied(LivingEntity entity, int amplifier) {
            if (entity instanceof PlayerEntity player) {
                this.player = player;
                EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                attributeInstance.removeModifier(Identifier.of(LEG_FRACTURE_UUID.toString()));

                // 计算移动速度减少量 (每级减少20%)
                double reduction = (amplifier + 1) * -0.20;
                attributeInstance.addPersistentModifier(new EntityAttributeModifier(
                        Identifier.of(LEG_FRACTURE_UUID.toString()),
                        reduction,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                );
            }
        }

        @Override
        public void onRemoved(AttributeContainer attributes) {
            if (this.player instanceof PlayerEntity) {
                EntityAttributeInstance speedAttribute = this.player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (speedAttribute != null) {
                    speedAttribute.removeModifier(Identifier.of(LEG_FRACTURE_UUID.toString())); // 确保移除修饰符
                }
            }
        }
    }

    // 流血
    public static class BleedingEffect extends StatusEffect {

        public BleedingEffect() {
            super(StatusEffectCategory.HARMFUL, 0x8B0000); // 棕色
        }

        @Override
        public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (entity instanceof PlayerEntity player) {
                if (player.isCreative() || player.isSpectator() || player.isInvulnerable()) {
                    return false;
                }
                if (entity.getHealth() > 1.0F) {
                    entity.setHealth(entity.getHealth() - 1.0F);
                }

                return true;
            }
            return false;
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            int interval = 100 / (amplifier + 1);
            return duration % interval == 0;
        }
    }

    public static class HeadacheEffect extends StatusEffect {
        private PlayerEntity player;

        public HeadacheEffect() {
            super(StatusEffectCategory.HARMFUL, 0x8B4513);
        }

        @Override
        public void onApplied(LivingEntity entity, int amplifier) {
            if (entity instanceof PlayerEntity player) {
                this.player = player;
                EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                attributeInstance.removeModifier(Identifier.of(HEADACHE_UUID.toString()));

                // 减少最大生命
                attributeInstance.addPersistentModifier(new EntityAttributeModifier(
                        Identifier.of(HEADACHE_UUID.toString()),
                        -4.0,
                        EntityAttributeModifier.Operation.ADD_VALUE)
                );
            }
        }

        @Override
        public void onRemoved(AttributeContainer attributes) {
            if (this.player instanceof PlayerEntity) {
                EntityAttributeInstance speedAttribute = this.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (speedAttribute != null) {
                    speedAttribute.removeModifier(Identifier.of(HEADACHE_UUID.toString())); // 确保移除修饰符
                }
            }
        }
    }

    public static class BackacheEffect extends StatusEffect {
        private PlayerEntity player;

        public BackacheEffect() {
            super(StatusEffectCategory.HARMFUL, 0x8B4513);
        }

        @Override
        public void onApplied(LivingEntity entity, int amplifier) {
            if (entity instanceof PlayerEntity player) {
                this.player = player;
                EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                attributeInstance.removeModifier(Identifier.of(BACKACHE_UUID.toString()));

                // 减少最大生命
                attributeInstance.addPersistentModifier(new EntityAttributeModifier(
                        Identifier.of(BACKACHE_UUID.toString()),
                        -2.0,
                        EntityAttributeModifier.Operation.ADD_VALUE)
                );
            }
        }

        @Override
        public void onRemoved(AttributeContainer attributes) {
            if (this.player instanceof PlayerEntity) {
                EntityAttributeInstance speedAttribute = this.player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (speedAttribute != null) {
                    speedAttribute.removeModifier(Identifier.of(BACKACHE_UUID.toString())); // 确保移除修饰符
                }
            }
        }
    }
}
