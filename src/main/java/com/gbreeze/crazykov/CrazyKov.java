package com.gbreeze.crazykov;

import com.gbreeze.crazykov.effects.ModEffects;
import com.gbreeze.crazykov.item.ModItems;
import com.gbreeze.crazykov.item.group.KovGroups;
import com.gbreeze.crazykov.stamina.StaminaHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrazyKov implements ModInitializer {
	public static final String MOD_ID = "crazykov";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 注册HUD渲染（客户端专用）
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			HudRenderCallback.EVENT.register(new StaminaHud());
		}
		// 注册物品组
		KovGroups.register();
		// 注册物品
		ModItems.register();
		// 注册效果
		ModEffects.register();

		LOGGER.info("CrazyKov MOD Initialized！");

	}


}