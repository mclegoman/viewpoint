/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.perspective;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = Camera.class)
public abstract class CameraMixin {
	@Shadow private boolean thirdPerson;
	@Shadow private Entity focusedEntity;

	@Shadow protected abstract void setRotation(float yaw, float pitch);

	@Shadow protected abstract void setPos(double x, double y, double z);

	@Shadow @Final private Vector3f verticalPlane;

	@Shadow private BlockView area;

	@Shadow private Vec3d pos;

	@Shadow protected abstract float clipToSpace(float f);

	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), method = "update")
	private float perspective$update(float original) {
		if (this.thirdPerson) {
			if (Perspective.isHoldingPerspective() || PerspectiveConfig.config.perspectiveMultiplier.value()) {
				switch (ClientData.minecraft.options.getPerspective()) {
					case THIRD_PERSON_BACK -> {
						return Perspective.getHoldPerspectiveBackMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveBackMultiplier()) : original;
					}
					case THIRD_PERSON_FRONT -> {
						return Perspective.getHoldPerspectiveFrontMultiplier() != 1.0F ? this.clipToSpace(original * Perspective.getHoldPerspectiveFrontMultiplier()) : original;
					}
				}
			}
		}
		return original;
	}
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"), method = "update")
	private void perspective$contributor_flip(Args args) {
		if (perspective$shouldFlip()) {
			args.set(1, (double)args.get(1) - (this.focusedEntity.getHeight() - this.focusedEntity.getEyeHeight(this.focusedEntity.getPose())));
		}
	}
	@Unique
	private boolean perspective$shouldFlip() {
		return Perspective.isHoldingPerspectiveFront() && this.focusedEntity != null && this.focusedEntity instanceof LivingEntity livingEntity && LivingEntityRenderer.shouldFlipUpsideDown(livingEntity);
	}

	@Inject(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V",
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private void perspective$topDown(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		if (!thirdPerson && Perspective.isHoldTop()) {
			Vec3d entityPos = focusedEntity.getLerpedPos(tickDelta);

			float f = 4.0F;
			float g = 1.0F;
			if (focusedEntity instanceof LivingEntity livingEntity) {
				g = livingEntity.getScale();
				f = (float)livingEntity.getAttributeValue(EntityAttributes.CAMERA_DISTANCE);
			}

			double y = (float) (entityPos.y + ((g * f) * Perspective.getMultiplier()));

			HitResult hitResult = this.area.raycast(new RaycastContext(this.pos, new Vec3d(this.pos.x, y, this.pos.z), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, this.focusedEntity));
			if (hitResult.getType() != HitResult.Type.MISS) {
				y = (float) hitResult.getPos().y;
			}

			this.setPos(entityPos.x, y, entityPos.z);
			this.setRotation(focusedEntity.getYaw(), 90.0F);
			ci.cancel();
		}
	}
	@Inject(method = "isThirdPerson", at = @At("RETURN"), cancellable = true)
	private void perspective$topDown_isThirdPerson(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && Perspective.isHoldTop()) cir.setReturnValue(true);
	}
}