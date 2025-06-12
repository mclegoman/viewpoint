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
	@ModifyArgs(method = "method_62215", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyRendering;renderCelestialBodies(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;FIFF)V"))
	private void perspective$renderStars(Args args) {
		args.set(5, (float) args.get(5) * PerspectiveConfig.config.starBrightnessMultiplier.value());
	}
}
