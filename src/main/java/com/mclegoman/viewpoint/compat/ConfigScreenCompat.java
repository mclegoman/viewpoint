/*
    viewpoint
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/viewpoint
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.compat;

import com.mclegoman.viewpoint.client.screen.config.ConfigScreen;
import net.minecraft.client.gui.screen.Screen;

public class ConfigScreenCompat {
	public static Screen getConfigEntryScreen(Screen parent) {
		return new ConfigScreen(parent, false, true, 1);
	}
}