/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hide;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hide.HideNameTagsDataLoader;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
	@Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void perspective$hide_nametag(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (ClientData.minecraft.gameRenderer.isRenderingPanorama() || PerspectiveConfig.config.hideNametags.value() || (livingEntity instanceof PlayerEntity && HideNameTagsDataLoader.registry.contains(String.valueOf((((PlayerEntity) livingEntity).getGameProfile().getId())))))
			cir.setReturnValue(false);
		if (livingEntity instanceof PlayerEntity) {
			if (Hide.shouldHidePlayer(((PlayerEntity) livingEntity).getGameProfile().getId())) cir.setReturnValue(false);
		}
	}
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void perspective$hide_nametag(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerEntity && Hide.shouldHidePlayer(((PlayerEntity) livingEntity).getGameProfile().getId())) ci.cancel();
	}
}