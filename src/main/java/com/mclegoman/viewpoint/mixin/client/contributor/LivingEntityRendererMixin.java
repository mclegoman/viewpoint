/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.contributor;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.contributor.ContributorData;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
	private static void perspective$shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {		
		if (entity instanceof PlayerEntity) {
			boolean shouldFlipUpsideDown = cir.getReturnValue();
			for (ContributorData contributor : Contributor.contributors) {
				if (contributor.getUuid().equals(((PlayerEntity) entity).getGameProfile().getId().toString()) &&
						contributor.getShouldFlipUpsideDown()) {
					shouldFlipUpsideDown = !shouldFlipUpsideDown;
					break;
				}
			}
			cir.setReturnValue(shouldFlipUpsideDown);
		} else {
			Text customName = entity.getCustomName();
			if (customName != null) {
				for (ContributorData contributor : Contributor.contributors) {
					if (contributor.getIds().contains(Formatting.strip(customName.getString()))) {
						cir.setReturnValue(true);
						break;
					}
				}
			}
		}
	}
}