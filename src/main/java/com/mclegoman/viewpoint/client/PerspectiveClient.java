/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client;

import com.mclegoman.viewpoint.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.viewpoint.client.config.PerspectiveConfigHelper;
import com.mclegoman.viewpoint.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.viewpoint.client.panorama.PerspectivePanorama;
import com.mclegoman.viewpoint.client.shaders.PerspectiveShader;
import com.mclegoman.viewpoint.client.textured_entity.PerspectiveTexturedEntity;
import com.mclegoman.viewpoint.client.util.PerspectiveKeybindings;
import com.mclegoman.viewpoint.client.util.PerspectiveResourcePacks;
import com.mclegoman.viewpoint.client.util.PerspectiveTick;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Initializing {}", PerspectiveData.ID);
			CrowdinTranslate.downloadTranslations(PerspectiveData.ID);
			PerspectiveConfigHelper.init();
			PerspectiveResourcePacks.init();
			PerspectiveKeybindings.init();
			PerspectiveShader.init();
			PerspectivePanorama.init();
			PerspectiveTexturedEntity.init();
			PerspectiveAprilFoolsPrank.init();
			PerspectiveHUDOverlays.init();
			PerspectiveTick.init();
		} catch (Exception error) {
			PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to initialize {}: {}", PerspectiveData.ID, error);
		}
	}
}