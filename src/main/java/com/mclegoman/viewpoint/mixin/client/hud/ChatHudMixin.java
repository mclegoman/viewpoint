/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hud;

import com.mclegoman.viewpoint.client.hud.HUDHelper;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = ChatHud.class)
public abstract class ChatHudMixin {
	@Inject(at = @At("RETURN"), method = "isChatHidden", cancellable = true)
	private void perspective$isChatHidden(CallbackInfoReturnable<Boolean> cir) {
		if (HUDHelper.shouldHideHUD()) cir.setReturnValue(true);
	}
}