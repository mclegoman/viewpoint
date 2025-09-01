/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.fov_perspective_hud;

import com.mclegoman.viewpoint.client.config.PerspectiveConfigHelper;
import com.mclegoman.viewpoint.client.data.PerspectiveClientData;
import com.mclegoman.viewpoint.client.zoom.PerspectiveZoom;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = Mouse.class)
public abstract class PerspectiveMouse {

    @Shadow private double eventDeltaWheel;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void perspective$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        try {
            if (PerspectiveClientData.CLIENT.currentScreen == null) {
                if (PerspectiveZoom.isZooming()) {
                    double d = (PerspectiveClientData.CLIENT.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * PerspectiveClientData.CLIENT.options.getMouseWheelSensitivity().getValue();
                    if (this.eventDeltaWheel != 0.0 && Math.signum(d) != Math.signum(this.eventDeltaWheel)) {
                        this.eventDeltaWheel = 0.0;
                    }
                    this.eventDeltaWheel += d;
                    int i = (int)this.eventDeltaWheel;
                    if (i == 0) {
                        return;
                    }
                    this.eventDeltaWheel -= i;
                    PerspectiveZoom.zoom(i > 0, (int)PerspectiveConfigHelper.getConfig("zoom_increment_size"));
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "An error occurred whilst trying to Mouse$onMouseScroll: {}", (Object)error);
        }
    }
    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        try {
            if (PerspectiveZoom.isZooming()) {
                if (button == 2) {
                    PerspectiveZoom.reset(PerspectiveClientData.CLIENT);
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn("{}An error occurred whilst trying to Mouse$onMouseButton: {}", PerspectiveData.PREFIX, error.getLocalizedMessage());
        }
    }
    
    @ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 1)
    private double perspective$updateXSensitivity(double x) {
        // Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
        if (PerspectiveZoom.isZooming()) {
            if (PerspectiveClientData.CLIENT.player != null) {
                double angle = MathHelper.cos((PerspectiveClientData.CLIENT.player.getPitch() / 180.0F) * MathHelper.PI);
                x = (x * (1.0F / Math.max((angle < 0) ? angle * -1.0F : angle, (Math.max(PerspectiveZoom.getZoomMultiplier(), 0.0F) + 1.0F) / 11.0F))) * PerspectiveZoom.getZoomMultiplier();
            }
        }
        return x;
    }
    @ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 2)
    private double perspective$updateYSensitivity(double y) {
        // Zoom.isZooming() checks Zoom.canZoom(), so we don't need to check it again.
        if (PerspectiveZoom.isZooming()) {
            return y * PerspectiveZoom.getZoomMultiplier();
        }
        return y;
    }
}