/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hud;

import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hide.HideHudTypes;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.config.ConfigHelper;

public class HUDHelper {
	public static void tick() {
		if (Keybindings.toggleVerOverlay.wasPressed()) {
			ConfigHelper.setConfig("version_overlay", !(boolean) ConfigHelper.getConfig("version_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.togglePosOverlay.wasPressed()) {
			ConfigHelper.setConfig("position_overlay", !(boolean) ConfigHelper.getConfig("position_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.toggleDayOverlay.wasPressed()) {
			ConfigHelper.setConfig("day_overlay", !(boolean) ConfigHelper.getConfig("day_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.toggleBiomeOverlay.wasPressed()) {
			ConfigHelper.setConfig("biome_overlay", !(boolean) ConfigHelper.getConfig("biome_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.toggleCPSOverlay.wasPressed()) {
			ConfigHelper.setConfig("cps_overlay", !(boolean) ConfigHelper.getConfig("cps_overlay"));
			ConfigHelper.saveConfig();
		}
	}
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.zoom) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveBack) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveFront);
	}
	public static int addY(int y) {
		return y + 12;
	}
}