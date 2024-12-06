/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hud;

import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hide.HideHudTypes;

public class HUDHelper {
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.zoom) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveBack) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveFront);
	}
	public static int addY(int y) {
		return y + 12;
	}
}