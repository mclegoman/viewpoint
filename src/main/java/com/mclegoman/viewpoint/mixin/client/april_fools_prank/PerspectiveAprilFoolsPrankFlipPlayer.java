/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.april_fools_prank;

import com.mclegoman.viewpoint.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.viewpoint.client.april_fools_prank.PerspectiveFlipDataLoader;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = LivingEntityRenderer.class)
public class PerspectiveAprilFoolsPrankFlipPlayer {
    @Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
    private static void perspective$shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (entity instanceof PlayerEntity player) {
                boolean isFlipped = cir.getReturnValue();
                if (PerspectiveFlipDataLoader.registry.contains(player.getGameProfile().getId().toString())) isFlipped = !isFlipped;
                if (PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools()) isFlipped = !isFlipped;
                cir.setReturnValue(isFlipped);
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set April Fools shouldFlipUpsideDown.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}