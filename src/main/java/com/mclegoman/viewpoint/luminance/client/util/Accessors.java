/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.util;

import com.mclegoman.viewpoint.mixin.client.luminance.GameRendererAccessor;
import com.mclegoman.viewpoint.client.data.ClientData;

public class Accessors {
	private static GameRendererAccessor gameRenderer;
	public static GameRendererAccessor getGameRenderer() {
		if (gameRenderer == null) {
			if (ClientData.minecraft.gameRenderer != null) gameRenderer = (GameRendererAccessor) ClientData.minecraft.gameRenderer;
		}
		return gameRenderer;
	}
}
