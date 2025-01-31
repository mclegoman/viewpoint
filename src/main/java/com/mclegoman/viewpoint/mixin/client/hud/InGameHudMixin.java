/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hud;

import com.mclegoman.viewpoint.client.hud.HUDHelper;
import com.mclegoman.viewpoint.client.hud.Overlays;
import com.mclegoman.viewpoint.config.ConfigHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow protected abstract void renderMiscOverlays(DrawContext context, float tickDelta);
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) {
			if (!(boolean) ConfigHelper.getConfig("hide_hud_hide_vignette")) {
				LayeredDrawer hideHudDrawer = (new LayeredDrawer()).addLayer(this::renderMiscOverlays);
				hideHudDrawer.render(context, tickDelta);
			}
			ci.cancel();
		}
	}
	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
		Overlays.renderOverlays(context);
	}
}