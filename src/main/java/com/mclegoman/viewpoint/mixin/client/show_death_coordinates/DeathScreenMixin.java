/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.show_death_coordinates;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.hud.Overlays;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = DeathScreen.class)
public class DeathScreenMixin {
	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (PerspectiveConfig.config.showDeathCoordinates.value() && ClientData.minecraft.player != null) {
			context.drawCenteredTextWithShadow(ClientData.minecraft.textRenderer, Translation.getTranslation(Data.getVersion().getID(), "show_death_coordinates", new Object[]{Overlays.getEntityPositionTextTitle(), Overlays.getEntityPositionTextDescription(ClientData.minecraft.player.getPos())}), ClientData.minecraft.getWindow().getScaledWidth() / 2, 115, 16777215);
		}
	}
}
