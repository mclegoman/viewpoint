/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hud;

import com.mclegoman.viewpoint.client.hud.HUDHelper;
import com.mclegoman.viewpoint.config.ConfigHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow protected abstract void renderMiscOverlays(DrawContext context, RenderTickCounter tickCounter);
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) {
			if (!(boolean) ConfigHelper.getConfig("hide_hud_hide_vignette")) {
				LayeredDrawer hideHudDrawer = (new LayeredDrawer()).addLayer(this::renderMiscOverlays);
				hideHudDrawer.render(context, tickCounter);
			}
			ci.cancel();
		}
	}
}