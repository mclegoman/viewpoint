/*
    viewpoint
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/viewpoint
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.util.Tick;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import net.fabricmc.api.ClientModInitializer;

public class Viewpoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing {}", Data.version.getName()));
			Contributor.init();
			Zoom.init();
			Keybindings.init();
			Tick.init();
			ConfigHelper.init();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to run onInitializeClient: {}", error));
		}
	}
}