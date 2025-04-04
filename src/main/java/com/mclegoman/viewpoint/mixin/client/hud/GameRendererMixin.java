/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hud;

import com.mclegoman.viewpoint.client.hud.Overlays;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Unique
	private boolean viewpoint$check;
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", ordinal = 0))
	private void perspective$check(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		this.viewpoint$check = true;
	}
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", ordinal = 1))
	private void perspective$beforeEffects(DrawContext context) {
		if (Overlays.canRender && this.viewpoint$check) Overlays.renderOverlays(context);
		Overlays.canRender = false;
		this.viewpoint$check = false;
		context.draw();
	}
}