package com.gbreeze.crazykov.stamina;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stamina {
    // 配置常量
    public static final float MAX_STAMINA = 100.0f;
    public static final float SPRINT_COST = 0.5f;  // 疾跑每秒消耗
    public static final float RECOVER_RATE = 0.3f; // 恢复速率
    public static final float MIN_SPRINT_STAMINA = 10.0f; // 开始疾跑所需最低体力

    private static final Map<UUID, Float> playerStamina = new HashMap<>();

    public static float getCurrentStamina(PlayerEntity player) {
        return playerStamina.computeIfAbsent(player.getUuid(), k -> MAX_STAMINA);
    }

    public static void setCurrentStamina(PlayerEntity player, float value) {
        playerStamina.put(player.getUuid(), MathHelper.clamp(value, 0, MAX_STAMINA));
    }

    public static boolean canStartSprinting(PlayerEntity player) {
        return getCurrentStamina(player) >= MIN_SPRINT_STAMINA;
    }

    public static void tick(PlayerEntity player) {
        float current = getCurrentStamina(player);

        if (player.isSprinting()) {
            // 疾跑时消耗体力
            setCurrentStamina(player, current - SPRINT_COST/20); // 20 ticks = 1秒
        } else {
            // 非疾跑时恢复体力
            setCurrentStamina(player, current + RECOVER_RATE/20);
        }
    }
}