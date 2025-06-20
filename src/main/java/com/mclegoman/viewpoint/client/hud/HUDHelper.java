/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hud;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.hide.HideHudTypes;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;

public class HUDHelper {
	public static void tick() {
		if (Keybindings.toggleVerOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.versionOverlay);
		if (Keybindings.togglePosOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.positionOverlay);
		if (Keybindings.toggleDayOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.dayOverlay);
		if (Keybindings.toggleBiomeOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.biomeOverlay);
		if (Keybindings.toggleDeathsOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.deathsOverlay);
		if (Keybindings.toggleTotemsOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.totemsOverlay);
		if (Keybindings.toggleCPSOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.cpsOverlay);
		if (Keybindings.toggleArmorOverlay.wasPressed()) PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.armorOverlay);
	}
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.zoom) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveBack) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveFront) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveTop);
	}
	public static boolean shouldHideHand() {
		return Hide.shouldHideHand(HideHudTypes.zoom);
	}
	public static int addY(int y) {
		return y + 12;
	}
}