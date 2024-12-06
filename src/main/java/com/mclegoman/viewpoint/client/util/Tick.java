/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.util;

import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.config.ConfigHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Tick {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (ConfigHelper.isFinishedInitializing()) {
				ConfigHelper.tick();
				Keybindings.tick();
				Perspective.tick();
				Zoom.tick();
			}
		});
	}
}