/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.luminance;

import com.mclegoman.viewpoint.luminance.client.events.Execute;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {
	@Inject(method = "render", at = @At("RETURN"))
	private void luminance$afterPanoramaRender(DrawContext context, int width, int height, float alpha, float tickDelta, CallbackInfo ci) {
		Execute.afterPanoramaRender();
	}
}
