/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.fov_perspective_hud;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.viewpoint.client.util.PerspectiveHideHUD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = ChatHud.class)
public abstract class PerspectiveChatHUD {
    @ModifyReturnValue(at = @At("RETURN"), method = "isChatHidden")
    private boolean perspective$isChatHidden(boolean isChatHidden) {
        return PerspectiveHideHUD.shouldHideHUD() || isChatHidden;
    }
}