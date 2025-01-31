/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.zoom;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Mouse.class)
public abstract class MouseMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
				boolean bl = ClientData.minecraft.options.getDiscreteMouseScroll().getValue();
				double d = ClientData.minecraft.options.getMouseWheelSensitivity().getValue();
				double f = (bl ? Math.signum(vertical) : vertical) * d;
				if (ClientData.minecraft.currentScreen == null && ClientData.minecraft.player != null) {
					Zoom.zoom((int) f, (int) ConfigHelper.getConfig("zoom_increment_size"));
					ci.cancel();
				}
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
	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 1)
	private double perspective$updateXSensitivity(double x) {
		// Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
		if (Zoom.isZooming()) {
			//if (Zoom.isSmoothCamera()) x = Zoom.smoothX.smooth(x * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
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
			//if (Zoom.isSmoothCamera()) y = Zoom.smoothY.smooth(y * Zoom.getMouseSensitivity(), Zoom.timeDelta * Zoom.getMouseSensitivity());
			if (Zoom.isScaled()) {
				return y * Zoom.getMultiplierFromFOV();
			}
		}
		return y;
	}
}