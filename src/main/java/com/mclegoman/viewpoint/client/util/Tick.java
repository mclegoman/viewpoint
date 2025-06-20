/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.util;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hud.HUDHelper;
import com.mclegoman.viewpoint.client.panorama.Panorama;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.client.toasts.PerspectiveToast;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.luminance.client.util.MessageOverlay;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Tick {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (client.isFinishedLoading()) {
				PerspectiveConfig.tick();
				HUDHelper.tick();
				Perspective.tick();
				Zoom.tick();
				Panorama.tick();
				Hide.tick();
				PerspectiveToast.Helper.tick();
				MessageOverlay.tick();
			}
		});
	}
}