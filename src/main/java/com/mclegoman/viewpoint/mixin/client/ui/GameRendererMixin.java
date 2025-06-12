/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.ui;

import com.mclegoman.viewpoint.client.ui.UIBackground;
import com.mclegoman.viewpoint.client.ui.UIBackgroundData;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Mutable
	@Shadow @Final private static Identifier BLUR_PROCESSOR;

	@Shadow protected abstract void loadBlurPostProcessor(ResourceFactory resourceFactory);

	@Inject(at = @At(value = "HEAD"), method = "renderBlur")
	private void perspective$loadShader(float delta, CallbackInfo ci) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (data.getShaderId() != null) {
			if (!BLUR_PROCESSOR.equals(data.getShaderId())) viewpoint$updateBlur(data.getShaderId());
		} else {
			if (!BLUR_PROCESSOR.equals(Identifier.ofVanilla("shaders/post/blur.json"))) viewpoint$updateBlur(Identifier.ofVanilla("shaders/post/blur.json"));
		}
	}
	@Inject(method = "renderBlur", at = @At("HEAD"), cancellable = true)
	private void perspective$renderShader(CallbackInfo ci) {
		if (!UIBackground.getCurrentUIBackground().getRenderShader()) ci.cancel();
	}
	@Inject(method = "loadPrograms", at = @At("RETURN"))
	private void viewpoint$loadPrograms(ResourceFactory factory, CallbackInfo ci) {
		this.viewpoint$factory = factory;
	}
	@Unique
	private ResourceFactory viewpoint$factory;
	@Unique
	private void viewpoint$updateBlur(Identifier id) {
		if (!BLUR_PROCESSOR.equals(id)) {
			BLUR_PROCESSOR = id;
			loadBlurPostProcessor(this.viewpoint$factory);
		}
	}
}