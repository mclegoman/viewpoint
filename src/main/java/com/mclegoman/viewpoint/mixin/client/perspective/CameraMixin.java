/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.perspective;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = Camera.class)
public abstract class CameraMixin {
	@Shadow protected abstract float clipToSpace(float f);
	@Shadow private boolean thirdPerson;
	@Shadow private Entity focusedEntity;
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), method = "update")
	private float perspective$update(float original) {
		if (this.thirdPerson) {
			if (Perspective.isHoldingPerspective() || PerspectiveConfig.config.perspectiveMultiplier.value()) {
				switch (ClientData.minecraft.options.getPerspective()) {
					case THIRD_PERSON_BACK -> {
						return Perspective.getHoldPerspectiveBackMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveBackMultiplier()) : original;
					}
					case THIRD_PERSON_FRONT -> {
						return Perspective.getHoldPerspectiveFrontMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveFrontMultiplier()) : original;
					}
				}
			}
		}
		return original;
	}
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"), method = "update")
	private void perspective$contributor_flip(Args args) {
		if (perspective$shouldFlip()) {
			args.set(1, (double)args.get(1) - (this.focusedEntity.getHeight() - this.focusedEntity.getEyeHeight(this.focusedEntity.getPose())));
		}
	}
	@Unique
	private boolean perspective$shouldFlip() {
		return Perspective.isHoldingPerspectiveFront() && this.focusedEntity != null && this.focusedEntity instanceof LivingEntity livingEntity && LivingEntityRenderer.shouldFlipUpsideDown(livingEntity);
	}
}