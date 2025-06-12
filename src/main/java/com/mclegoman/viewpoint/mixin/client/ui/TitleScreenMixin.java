/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.ui;

import com.mclegoman.viewpoint.luminance.client.events.Execute;
import com.mclegoman.viewpoint.mixin.client.luminance.GameRendererAccessor;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.ui.UIBackground;
import com.mclegoman.viewpoint.client.ui.UIBackgroundData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = TitleScreen.class)
public abstract class TitleScreenMixin {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;renderPanoramaBackground(Lnet/minecraft/client/gui/DrawContext;F)V"))
	private void perspective$renderTitleScreen(TitleScreen instance, DrawContext context, float v) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (data.getRenderTitleScreen() != null) data.getRenderTitleScreen().run(context);
		if (data.getRenderTitleScreenPanorama()) {
			instance.renderPanoramaBackground(context, v);
		} else {
			// Since we're disabling the panorama, we have to execute afterPanoramaRender.
			Execute.afterPanoramaRender(((GameRendererAccessor) ClientData.minecraft.gameRenderer).getPool());
		}
	}
}