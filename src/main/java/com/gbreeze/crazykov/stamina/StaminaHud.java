package com.gbreeze.crazykov.stamina;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class StaminaHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        PlayerEntity player = client.player;
        float stamina = Stamina.getCurrentStamina(player);
        float maxStamina = Stamina.MAX_STAMINA;
        float ratio = MathHelper.clamp(stamina / maxStamina, 0.0f, 1.0f);

        // 屏幕尺寸和位置计算
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // 在经验条上方绘制（兼容1.21.1 UI布局）
        int barWidth = 182;
        int barHeight = 5;
        int x = screenWidth / 2 - barWidth / 2;
        int y = screenHeight - 49; // 经验条上方10像素

        // 绘制半透明背景（60%不透明度）
        drawContext.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0x60000000);

        // 绘制动态体力条（颜色渐变）
        int filledWidth = (int)(barWidth * ratio);
        int color = getStaminaColor(ratio);
        drawContext.fill(x, y, x + filledWidth, y + barHeight, color);

        // 绘制体力数值（居中显示）
        String text = String.format("%.0f", stamina);
        int textX = screenWidth / 2 - client.textRenderer.getWidth(text) / 2;
        int textY = y - 10; // 数值位于体力条上方
        drawContext.drawTextWithShadow(client.textRenderer, text, textX, textY, 0xFFFFFFFF);
    }

    // 动态颜色计算：绿(100%) → 黄(50%) → 红(0%)
    private int getStaminaColor(float ratio) {
        int red, green;
        if (ratio > 0.5f) {
            red = (int)(255 * (1 - (ratio - 0.5f) * 2)); // 红减少
            green = 255;
        } else {
            red = 255;
            green = (int)(255 * ratio * 2); // 绿减少
        }
        return 0xFF000000 | (red << 16) | (green << 8); // ARGB格式
    }
}