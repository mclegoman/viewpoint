/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.perspective;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@ModifyExpressionValue(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
	private boolean perspective$topDownCrosshair(boolean original) {
		return (!original || !Perspective.isHoldTop()) && original;
	}
}