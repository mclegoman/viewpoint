/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.perspective;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(priority = 100, value = Camera.class)
public abstract class CameraMixin {
	@Shadow protected abstract float clipToSpace(float f);
	@Shadow private boolean thirdPerson;
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), method = "update")
	private float perspective$update(float original) {
		if (this.thirdPerson && Perspective.isHoldingPerspective()) {
			if (Perspective.isHoldingPerspectiveBack()) {
				return Perspective.getHoldPerspectiveBackMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveBackMultiplier()) : original;
			}
			else if (Perspective.isHoldingPerspectiveFront()) {
				return Perspective.getHoldPerspectiveFrontMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveFrontMultiplier()) : original;
			}
		}
		return original;
	}
}