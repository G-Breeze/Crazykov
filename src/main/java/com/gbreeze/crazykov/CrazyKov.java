package com.gbreeze.crazykov;

import com.gbreeze.crazykov.stamina.Stamina;
import com.gbreeze.crazykov.stamina.StaminaHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrazyKov implements ModInitializer {
	public static final String MOD_ID = "crazykov";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Map<UUID, Boolean> lastSprintStates = new HashMap<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// 使用新版Tick事件注册方式
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			PlayerEntity player = client.player;
			boolean isSprinting = player.isSprinting();
			Boolean wasSprinting = lastSprintStates.get(player.getUuid());

			// 检测尝试开始疾跑的情况
			if (!Boolean.TRUE.equals(wasSprinting) && isSprinting) {
				if (!Stamina.canStartSprinting(player)) {
					player.setSprinting(false);
					player.sendMessage(Text.translatable("message.crazykov.low_stamina")
							.formatted(Formatting.RED), true);
				}
			}

			// 更新体力
			Stamina.tick(player);
			lastSprintStates.put(player.getUuid(), isSprinting);

			// 体力耗尽时强制停止疾跑
			if (isSprinting && Stamina.getCurrentStamina(player) <= 0) {
				player.setSprinting(false);
			}
		});

		// 注册HUD渲染（客户端专用）
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			HudRenderCallback.EVENT.register(new StaminaHud());
		}

		LOGGER.info("体力模组初始化完成！");

	}

}