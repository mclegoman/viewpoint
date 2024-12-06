/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.contributor;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.contributor.ContributorData;
import com.mclegoman.viewpoint.client.texture.TextureHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(priority = 100, value = AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
	@Shadow
	@Nullable
	private PlayerListEntry playerListEntry;

	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
		if (this.playerListEntry != null) {
			SkinTextures currentSkinTextures = cir.getReturnValue();
			Identifier skinTexture = currentSkinTextures.texture();
			Identifier capeTexture = currentSkinTextures.capeTexture();
			SkinTextures.Model model = currentSkinTextures.model();
			UUID uuid = this.playerListEntry.getProfile().getId();
			String stringifiedUUID = String.valueOf(uuid);
			for (ContributorData developer : Contributor.contributors) {
				if (developer.getUuid().equals(stringifiedUUID)) {
					if (developer.getShouldReplaceCape()) {
						capeTexture = TextureHelper.getTexture(developer.getCapeTexture(), capeTexture);
					}
				}
			}
			cir.setReturnValue(new SkinTextures(skinTexture, currentSkinTextures.textureUrl(), capeTexture, capeTexture, model, currentSkinTextures.secure()));
		}
	}
}