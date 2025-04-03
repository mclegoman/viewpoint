/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.util;

import com.mclegoman.viewpoint.client.hud.HUDHelper;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.panorama.Panorama;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.config.ConfigHelper;

public class Tick {
	public static void init() {
	}
	public static void tick() {
		if (ConfigHelper.isFinishedInitializing()) {
			ConfigHelper.tick();
			Keybindings.tick();
			Perspective.tick();
			Zoom.tick();
			Panorama.tick();
			HUDHelper.tick();
		}
	}
}