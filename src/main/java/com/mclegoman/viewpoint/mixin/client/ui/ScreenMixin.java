/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.ui;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.ui.UIBackground;
import com.mclegoman.viewpoint.client.ui.UIBackgroundData;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Screen.class)
public abstract class ScreenMixin extends AbstractParentElement {
	@Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;applyBlur(Lnet/minecraft/client/gui/DrawContext;)V"))
	private void perspective$renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (!viewpoint$isTitleScreen()) {
			UIBackgroundData data = UIBackground.getCurrentUIBackground();
			if (ClientData.minecraft.world != null && data.getRenderWorld() != null) data.getRenderWorld().run(context);
		}
	}
	@Inject(method = "renderPanoramaBackground", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$renderPanoramaBackground(DrawContext context, float delta, CallbackInfo ci) {
		if (!viewpoint$isTitleScreen()) {
			UIBackgroundData data = UIBackground.getCurrentUIBackground();
			if (data.getRenderMenu() != null) data.getRenderMenu().run(context);
			if (!data.getRenderPanorama()) ci.cancel();
		}
	}
	@Inject(method = "renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$renderDarkening(DrawContext context, int x, int y, int width, int height, CallbackInfo ci) {
		if (!viewpoint$isTitleScreen() && !UIBackground.getCurrentUIBackground().getRenderDarkening()) ci.cancel();
	}
	@Unique
	private boolean viewpoint$isTitleScreen() {
		return (Screen)(Object)this instanceof TitleScreen;
	}
}