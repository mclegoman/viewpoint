/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.appearance;

import com.mclegoman.viewpoint.client.appearance.Appearance;
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

	@Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
	private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
		if (this.playerListEntry != null) {
			SkinTextures currentSkinTextures = cir.getReturnValue();
			Identifier skinTexture = currentSkinTextures.texture();
			Identifier capeTexture = currentSkinTextures.capeTexture();
			SkinTextures.Model model = currentSkinTextures.model();
			UUID uuid = this.playerListEntry.getProfile().getId();
			String stringifiedUUID = String.valueOf(uuid);
			Appearance.Data appearance = Appearance.DataLoader.registry.get(stringifiedUUID);
			if (appearance != null) {
				skinTexture = TextureHelper.getTexture(appearance.texture(), skinTexture);
				model = appearance.model();
			}
			ContributorData developer = Contributor.getContributorData(stringifiedUUID);
			if (developer != null && developer.getShouldReplaceCape()) capeTexture = TextureHelper.getTexture(developer.getCapeTexture(), capeTexture);
			cir.setReturnValue(new SkinTextures(skinTexture, currentSkinTextures.textureUrl(), capeTexture, capeTexture, model, currentSkinTextures.secure()));
		}
	}
}