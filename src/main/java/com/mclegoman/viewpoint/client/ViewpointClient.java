/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client;

import com.mclegoman.viewpoint.client.commands.Commands;
import com.mclegoman.viewpoint.luminance.client.util.MessageOverlay;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.appearance.Appearance;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hud.Overlays;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.logo.PerspectiveLogo;
import com.mclegoman.viewpoint.client.panorama.Panorama;
import com.mclegoman.viewpoint.client.texture.TextureHelper;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.client.ui.UIBackground;
import com.mclegoman.viewpoint.client.util.Tick;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.common.data.Data;
import net.fabricmc.api.ClientModInitializer;

public class ViewpointClient implements ClientModInitializer {
	public void onInitializeClient() {
		try {
			Data.getVersion().sendToLog(LogType.INFO, Translation.getString("Initializing {}:client", Data.getVersion().getName()));
			MessageOverlay.init();
			PerspectiveConfig.init();
			Appearance.init();
			UIBackground.init();
			Overlays.init();
			Zoom.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			PerspectiveLogo.init();
			Tick.init();
			Commands.init();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to run client:init: {}", error));
		}
	}
}