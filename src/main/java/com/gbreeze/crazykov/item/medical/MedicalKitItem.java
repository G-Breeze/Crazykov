package com.gbreeze.crazykov.item.medical;

import com.gbreeze.crazykov.item.ItemRarity;
import com.gbreeze.crazykov.item.ModularItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class MedicalKitItem extends ModularItem {
    private final int MAX_USES;
    private final int WARMUP_TICKS;
    private final int HEAL_INTERVAL;
    private final int HEAL_PER_TIME;

    public MedicalKitItem(Settings settings, ItemRarity rarity, float weight, Integer maxDamage, int warmupTicks, int healInterval, int healPerTick) {
        super(settings.maxDamage(maxDamage), rarity, weight, 1);
        this.MAX_USES = maxDamage;
        this.WARMUP_TICKS = warmupTicks;
        this.HEAL_INTERVAL = healInterval;
        this.HEAL_PER_TIME = healPerTick;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (stack.getDamage() >= stack.getMaxDamage()) {
            user.sendMessage(Text.literal("医疗包已耗尽！"), true);
            return TypedActionResult.fail(stack);
        }

        if (user.getHealth() >= user.getMaxHealth()) {
            user.sendMessage(Text.literal("不需要治疗！"), true);
            return TypedActionResult.fail(stack);
        }

        // 只开始使用，不立即治疗
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int usedTime = remainingUseTicks*-1;

        // 前摇阶段
        if (usedTime < WARMUP_TICKS) {
            // 前摇动画效果
            if (usedTime % 5 == 0) { // 每5tick播放一次声音
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.BLOCK_WOOL_PLACE,
                        SoundCategory.PLAYERS,
                        0.5f, 0.5f + usedTime * 0.02f);
            }
        } else {
            // 治疗阶段
            if (usedTime % HEAL_INTERVAL == 0) {
                // 检查终止条件
                if (stack.getDamage() >= stack.getMaxDamage() ||
                        user.getHealth() >= user.getMaxHealth()) {
                    user.stopUsingItem();
                    return;
                }

                // 执行治疗
                user.heal(HEAL_PER_TIME);
                stack.damage((int) HEAL_PER_TIME, user, getEquipmentSlot(user.getActiveHand()));

                // 治疗效果
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_GENERIC_DRINK,
                        SoundCategory.PLAYERS,
                        0.8f, 1.2f);

                if (world.isClient) {
                    spawnHealParticles(user);
                }
            }
        }
    }

    private EquipmentSlot getEquipmentSlot(Hand hand) {
        return hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    private boolean shouldStopHealing(LivingEntity user, ItemStack stack) {
        return stack.getDamage() >= stack.getMaxDamage() ||
                user.getHealth() >= user.getMaxHealth();
    }

    private void playWarmupSound(LivingEntity user) {
        user.getWorld().playSound(user, user.getBlockPos(),
                SoundEvents.BLOCK_WOOL_PLACE,
                SoundCategory.PLAYERS,
                0.8f,
                0.5f + user.getRandom().nextFloat() * 0.5f
        );
    }

    private void playHealEffects(World world, LivingEntity user) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_GENERIC_DRINK,
                SoundCategory.PLAYERS,
                1.0f,
                1.2f
        );

        if (world.isClient) {
            spawnHealParticles(user);
        }
    }

    private void spawnHealParticles(LivingEntity user) {
        ClientWorld world = (ClientWorld) user.getWorld();
        for (int i = 0; i < 3; i++) {
            world.addParticle(ParticleTypes.HEART,
                    user.getX() + (world.random.nextDouble() - 0.5) * 1.5,
                    user.getY() + 1.0 + world.random.nextDouble(),
                    user.getZ() + (world.random.nextDouble() - 0.5) * 1.5,
                    0, 0.05, 0);
        }
    }

    public int getMaxUseTime(ItemStack stack) {
        return (int) (MAX_USES/((float) 20 /HEAL_INTERVAL*HEAL_PER_TIME))*20; // 最大使用时间(tick)
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (stack.getDamage() >= stack.getMaxDamage()) {
            stack.decrement(1); // 数量减1（销毁）
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.PLAYERS,
                    0.8f, 1.0f);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.crazykov.medical.remain", this.MAX_USES - stack.getDamage()).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.crazykov.medical.preparation", this.WARMUP_TICKS/20.0f).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.crazykov.medical.interval", (float) this.HEAL_INTERVAL/20.0f).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.crazykov.medical.heal", this.HEAL_PER_TIME).formatted(Formatting.GOLD));
    }
}
