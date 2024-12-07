/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.compat;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;

public class CatalogueCompat {
	public static Screen createConfigScreen(Screen currentScreen, ModContainer mod) {
		return ConfigScreenCompat.getConfigEntryScreen(currentScreen);
	}
}