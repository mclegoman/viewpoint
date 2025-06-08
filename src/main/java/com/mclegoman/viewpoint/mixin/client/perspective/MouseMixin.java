/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.perspective;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.config.ConfigHelper;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.Scroller;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Mouse.class)
public abstract class MouseMixin {
	@Shadow @Final private Scroller scroller;
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void perspective$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Perspective.isHoldingPerspective() && Perspective.isHoldingAdjust()) {
			boolean discreteMouseScroll = ClientData.minecraft.options.getDiscreteMouseScroll().getValue();
			double mouseWheelSensitivity = ClientData.minecraft.options.getMouseWheelSensitivity().getValue();
			double calculatedScroll = (discreteMouseScroll ? Math.signum(vertical) : vertical) * mouseWheelSensitivity;
			Vector2i vector2i = this.scroller.update(calculatedScroll, calculatedScroll);
			if (vector2i.y != 0) {
				Perspective.adjust(-vector2i.y / 100.0F, (int)ConfigHelper.getConfig("hold_perspective_multiplier_increment_size"));
				ci.cancel();
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Perspective.isHoldingPerspective() && Perspective.isHoldingAdjust()) {
			if (button == 2) {
				Perspective.reset();
				ci.cancel();
			}
		}
	}
}