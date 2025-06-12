/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.sky;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = WorldRenderer.class)
public class WorldRendererMixin {
	@ModifyArgs(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 3))
	private void perspective$renderStars(Args args) {
		for (int i = 0; i < args.size(); i++) args.set(i, (float) args.get(i) * PerspectiveConfig.config.starBrightnessMultiplier.value());
	}
}
