/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client;

import com.mclegoman.viewpoint.client.util.Tick;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Inject(method = "tick", at = @At("RETURN"))
	private void viewpoint$tick(CallbackInfo ci) {
		Tick.tick();
	}
}