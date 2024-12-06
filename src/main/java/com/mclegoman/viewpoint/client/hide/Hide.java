/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hide;

import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.config.ConfigHelper;

public class Hide {
	public static final String[] hideCrosshairModes = new String[]{"vanilla", "dynamic", "hidden"};
	public static boolean shouldHideHud(HideHudTypes type) {
		switch (type) {
			case zoom -> {return Zoom.isZooming() && (boolean) ConfigHelper.getConfig("zoom_hide_hud");}
			case holdPerspectiveBack -> {return Perspective.isHoldingPerspectiveBack() && (boolean) ConfigHelper.getConfig("hold_perspective_back_hide_hud");}
			case holdPerspectiveFront -> {return Perspective.isHoldingPerspectiveFront() && (boolean) ConfigHelper.getConfig("hold_perspective_front_hide_hud");}
			default -> {return false;}
		}
	}
}