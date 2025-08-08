package com.gbreeze.crazykov.mixin;

import com.gbreeze.crazykov.stamina.Stamina;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class StaminaSprintingMixin {
	@Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
	private void onTickMovement(CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity player) {
			Stamina.tickSprint(ci, player);
		}
	}
}
