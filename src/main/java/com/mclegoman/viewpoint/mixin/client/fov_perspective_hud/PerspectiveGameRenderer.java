/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.fov_perspective_hud;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.viewpoint.client.config.PerspectiveConfigHelper;
import com.mclegoman.viewpoint.client.data.PerspectiveClientData;
import com.mclegoman.viewpoint.client.util.PerspectiveHideHUD;
import com.mclegoman.viewpoint.client.zoom.PerspectiveZoom;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class PerspectiveGameRenderer {
    @Shadow public abstract boolean isRenderingPanorama();
    @Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
    private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) cir.setReturnValue(false);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderCrosshair.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D"), method = "renderHand")
    private double perspective$renderHand(double fov) {
        return PerspectiveZoom.fov;
    }
    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double perspective$getFov(double fov, Camera camera, float tickDelta, boolean changingFov) {
        PerspectiveZoom.fov = fov;
        double newFOV = fov;
        if (!this.isRenderingPanorama()) {
            if (PerspectiveZoom.isZooming()) {
                if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("instant")) {
                    newFOV *= PerspectiveZoom.getZoomMultiplier();
                }
            }
            if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth")) {
                newFOV *= MathHelper.lerp(tickDelta, PerspectiveZoom.prevZoomMultiplier, PerspectiveZoom.zoomMultiplier);
            }
        }
        return PerspectiveZoom.limitFov(newFOV);
    }
    @ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
    private Object perspective$getDamageTiltStrength(Object value) {
        return (value instanceof Double) ? ((Double)value) * Math.max(PerspectiveZoom.getZoomMultiplier(), 0.001) : value;
    }
    @ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
    private float perspective$getDamageTiltYaw(float value) {
        return (float) (value * Math.max(PerspectiveZoom.getZoomMultiplier(), 0.001));
    }
    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void perspective$bobViewStrideDistance(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (PerspectiveClientData.CLIENT.player != null) {
            float f = PerspectiveClientData.CLIENT.player.strideDistance - PerspectiveClientData.CLIENT.player.prevStrideDistance;
            float g = -(PerspectiveClientData.CLIENT.player.strideDistance + f * tickDelta);
            float h = (float) (MathHelper.lerp(tickDelta, PerspectiveClientData.CLIENT.player.prevStrideDistance, PerspectiveClientData.CLIENT.player.strideDistance) * Math.max(PerspectiveZoom.getZoomMultiplier(), 0.001));
            matrices.translate(MathHelper.sin(g * 3.1415927F) * h * 0.5F, -Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F));
        }
        ci.cancel();
    }
}