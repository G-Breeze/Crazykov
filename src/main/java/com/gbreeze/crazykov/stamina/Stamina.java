package com.gbreeze.crazykov.stamina;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stamina {
    // 配置常量
    public static final float MAX_STAMINA = 100.0f;
    public static final Integer MAX_RECOVER_TICK = 30;
    public static final float SPRINT_COST = 3f;  // 疾跑每秒消耗
    public static final float JUMP_COST = 2.5f;  // 跳跃消耗
    public static final float RECOVER_RATE = 3.5f; // 恢复速率
    public static final float MIN_SPRINT_STAMINA = 10.0f; // 开始疾跑所需最低体力
    public static boolean wasSprinting = false; // 记录上一tick的疾跑状态

    private static final Map<UUID, Float> playerStamina = new HashMap<>();
    private static final Map<UUID, Integer> playerStaminaRecoverTick = new HashMap<>();

    public static float getCurrentStamina(PlayerEntity player) {
        return playerStamina.computeIfAbsent(player.getUuid(), k -> MAX_STAMINA);
    }

    public static void setCurrentStamina(PlayerEntity player, float value) {
        playerStamina.put(player.getUuid(), MathHelper.clamp(value, 0, MAX_STAMINA));
    }

    public static Integer getStopSprintingTick(PlayerEntity player) {
        return playerStaminaRecoverTick.computeIfAbsent(player.getUuid(), k -> 0);
    }

    public static void setStopSprintingTick(PlayerEntity player, Integer value) {
        playerStaminaRecoverTick.put(player.getUuid(), MathHelper.clamp(value, 0, MAX_RECOVER_TICK));
    }

    public static boolean canStartSprinting(PlayerEntity player) {
        return getCurrentStamina(player) >= MIN_SPRINT_STAMINA;
    }

    private static boolean shouldIgnorePlayer(PlayerEntity player) {
        return player.isCreative() || player.isSpectator();
    }

    public static void tickSprint(CallbackInfo ci, PlayerEntity player) {
        boolean isSprinting = player.isSprinting();
        float current = getCurrentStamina(player);
        if (shouldIgnorePlayer(player)) return;
        // 更新体力
        // 体力耗尽时强制停止疾跑
        if ((isSprinting && Stamina.getCurrentStamina(player) <= 0) || (!wasSprinting && !canStartSprinting(player))) {
            player.setSprinting(false);
            player.sendMessage(Text.translatable("message.crazykov.low_stamina")
                    .formatted(Formatting.RED), true);
        }
        if (player.isSprinting()) {
            // 疾跑时消耗体力
            setCurrentStamina(player, current - SPRINT_COST/20); // 20 ticks = 1秒
            setStopSprintingTick(player, 0);
        } else {
            // 非疾跑时恢复体力,冷却
            if (getStopSprintingTick(player) == MAX_RECOVER_TICK) {
                setCurrentStamina(player, current + RECOVER_RATE/20);
            } else {
                setStopSprintingTick(player, getStopSprintingTick(player) + 1);
            }
        }
        wasSprinting = player.isSprinting();
    }

    public static void tickJump(CallbackInfo ci, PlayerEntity player) {
        float current = getCurrentStamina(player);
        if (shouldIgnorePlayer(player)) return;
        // 更新体力
        setCurrentStamina(player, current - JUMP_COST / 2);
        setStopSprintingTick(player, 0);
        wasSprinting = player.isSprinting();
    }
}