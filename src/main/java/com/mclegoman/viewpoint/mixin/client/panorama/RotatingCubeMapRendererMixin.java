package com.mclegoman.viewpoint.mixin.client.panorama;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = RotatingCubeMapRenderer.class)
public abstract class RotatingCubeMapRendererMixin {
	@Shadow private float pitch;
	@Inject(method = "render", at = @At(value = "HEAD"))
	private void viewpoint$prep(DrawContext context, int width, int height, boolean rotate, CallbackInfo ci) {
		// Pitch
		float pitch = this.viewpoint$pitch;
		if (rotate) {
			pitch = pitch + ((float)((double)ClientData.minecraft.getRenderTickCounter().getFixedDeltaTicks() * ClientData.minecraft.options.getPanoramaSpeed().getValue())) * PerspectiveConfig.config.panoramaPitchSpeed.value();
		}
		// Yaw
		boolean bouncePanorama = PerspectiveConfig.config.bouncePanoramaYaw.value();
		float yaw = 10.0F;
		if (!bouncePanorama && this.viewpoint$processedYaw != 10.0F) {
			yaw = MathHelper.lerp(ClientData.minecraft.getRenderTickCounter().getFixedDeltaTicks(), this.viewpoint$processedYaw, 10.0F);
		}
		if (bouncePanorama && rotate) {
			this.viewpoint$yaw += ClientData.minecraft.getRenderTickCounter().getFixedDeltaTicks() * (float)((double)ClientData.minecraft.options.getPanoramaSpeed().getValue()) * PerspectiveConfig.config.bouncePanoramaYawSpeed.value();
			yaw = (float)(Math.sin(this.viewpoint$yaw) * PerspectiveConfig.config.bouncePanoramaYawAngle.value());
		}
		yaw = ((this.viewpoint$processedYaw + yaw) * 0.5F);
		this.viewpoint$processedYaw = MathHelper.lerp(ClientData.minecraft.getRenderTickCounter().getFixedDeltaTicks(), this.viewpoint$processedYaw, yaw);

		pitch = ((this.viewpoint$pitch + pitch) * 0.5F);
		this.viewpoint$pitch = MathHelper.lerp(ClientData.minecraft.getRenderTickCounter().getFixedDeltaTicks(), this.viewpoint$pitch, pitch) % 360.0F;
	}
	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/CubeMapRenderer;draw(Lnet/minecraft/client/MinecraftClient;FF)V"))
	private void viewpoint$render(Args args) {
		args.set(1, this.viewpoint$processedYaw);
		args.set(2, -this.viewpoint$pitch);
	}
	@Unique
	private float viewpoint$yaw = (float)(Math.asin(10.0F / PerspectiveConfig.config.bouncePanoramaYawAngle.value()));
	@Unique float viewpoint$processedYaw = 10.0F;
	@Unique float viewpoint$pitch = this.pitch;
}