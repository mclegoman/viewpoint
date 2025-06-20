/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.zoom;

import com.llamalad7.mixinextras.sugar.Local;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Mouse.class)
public abstract class MouseMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci, @Local(name = "vector2i") Vector2i vector2i) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (vector2i.y != 0) {
				Zoom.zoom(vector2i.y, PerspectiveConfig.config.zoomIncrementSize.value());
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (button == 2) {
				Zoom.reset();
				ci.cancel();
			}
		}
	}

	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	private void perspective$updateTime(double timeDelta, CallbackInfo ci) {
		if (Zoom.canZoom() && Zoom.isSmoothCamera()) Zoom.timeDelta = timeDelta;
	}
	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 1)
	private double perspective$updateXSensitivity(double x) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (Zoom.isSmoothCamera()) x = Zoom.smoothX.smooth(x * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
			if (Zoom.isScaled() && ClientData.minecraft.player != null) {
				double angle = MathHelper.cos((ClientData.minecraft.player.getPitch() / 180.0F) * MathHelper.PI);
				x = (x * (1.0F / Math.max((angle < 0) ? angle * -1.0F : angle, (Math.max(Zoom.getMultiplierFromFOV(), 0.0F) + 1.0F) / 11.0F))) * Zoom.getMultiplierFromFOV();
			}
		}
		return x;
	}
	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 2)
	private double perspective$updateYSensitivity(double y) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (Zoom.isSmoothCamera()) y = Zoom.smoothY.smooth(y * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
			if (Zoom.isScaled()) {
				return y * Zoom.getMultiplierFromFOV();
			}
		}
		return y;
	}
}