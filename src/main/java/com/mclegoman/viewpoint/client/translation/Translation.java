/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.translation;

import com.mclegoman.viewpoint.client.zoom.Zoom;
import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;

public class Translation extends com.mclegoman.viewpoint.luminance.Translation {
	public static MutableText getTimeOverlayTranslation(String namespace, String key) {
		return getConfigTranslation(namespace, "time_overlay.type." + key);
	}
	public static MutableText getVariableTranslation(String namespace, boolean toggle, Type type) {
		return toggle ? getTranslation(namespace, "variable." + type.asString() + ".on") : getTranslation(namespace, "variable." + type.asString() + ".off");
	}
	public static MutableText getZoomTransitionTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("instant")) return getConfigTranslation(namespace, "zoom.transition.instant");
		else if (key.equalsIgnoreCase("smooth")) return getConfigTranslation(namespace, "zoom.transition.smooth");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getZoomScaleModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("scaled")) return getConfigTranslation(namespace, "zoom.scale_mode.scaled");
		else if (key.equalsIgnoreCase("vanilla")) return getConfigTranslation(namespace, "zoom.scale_mode.vanilla");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getZoomTypeTranslation(String namespace, String zoomType, boolean hover) {
		if (Zoom.isValidZoomType(Zoom.getZoomType())) {
			return getConfigTranslation(namespace, "zoom.type." + zoomType + (hover ? ".hover" : ""));
		}
		return getErrorTranslation(namespace);
	}
	public static MutableText getZoomTypeTranslation(String namespace, String zoomType) {
		return getZoomTypeTranslation(namespace, zoomType, false);
	}
	public enum Type implements StringIdentifiable {
		ENDISABLE("endisable"),
		ONFF("onff"),
		DISABLE_SCREEN_MODE("disable_screen_mode"),
		BLUR("blur");
		private final String name;
		Type(String name) {
			this.name = name;
		}
		@Override
		public String asString() {
			return this.name;
		}
	}
}