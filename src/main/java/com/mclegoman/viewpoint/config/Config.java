/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.config;

import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.Couple;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import com.mclegoman.viewpoint.magistermaks.SimpleConfig;

public class Config {
	protected static final String id = "viewpoint";
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static boolean zoomEnabled;
	protected static int zoomLevel;
	protected static int zoomIncrementSize;
	protected static String zoomTransition;
	protected static double zoomSmoothSpeedIn;
	protected static double zoomSmoothSpeedOut;
	protected static String zoomScaleMode;
	protected static boolean zoomHideHud;
	protected static boolean zoomShowPercentage;
	protected static String zoomType;
	protected static boolean zoomReset;
	protected static boolean zoomCinematic;
	protected static double holdPerspectiveBackMultiplier;
	protected static double holdPerspectiveFrontMultiplier;
	protected static boolean holdPerspectiveBackHideHud;
	protected static boolean holdPerspectiveFrontHideHud;
	protected static boolean hideHudVignette;
	protected static boolean versionOverlay;
	protected static boolean positionOverlay;
	protected static String timeOverlay;
	protected static boolean dayOverlay;
	protected static boolean biomeOverlay;
	protected static boolean cpsOverlay;
	protected static final Object[] options;

	protected static void init() {
		try {
			configProvider = new ConfigProvider();
			create();
			config = SimpleConfig.of(id).provider(configProvider).request();
			assign();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to initialize {} config: {}", id, error));
		}
	}

	protected static void create() {
		configProvider.add(new Couple<>("zoom_enabled", true));
		configProvider.add(new Couple<>("zoom_level", 40));
		configProvider.add(new Couple<>("zoom_increment_size", 2));
		configProvider.add(new Couple<>("zoom_transition", "smooth"));
		configProvider.add(new Couple<>("zoom_smooth_speed_in", 1.0D));
		configProvider.add(new Couple<>("zoom_smooth_speed_out", 1.0D));
		configProvider.add(new Couple<>("zoom_scale_mode", "scaled"));
		configProvider.add(new Couple<>("zoom_hide_hud", true));
		configProvider.add(new Couple<>("zoom_show_percentage", false));
		configProvider.add(new Couple<>("zoom_type", "viewpoint:logarithmic"));
		configProvider.add(new Couple<>("zoom_reset", false));
		configProvider.add(new Couple<>("zoom_cinematic", false));
		configProvider.add(new Couple<>("hold_perspective_back_multiplier", 1.0D));
		configProvider.add(new Couple<>("hold_perspective_front_multiplier", 1.0D));
		configProvider.add(new Couple<>("hold_perspective_back_hide_hud", true));
		configProvider.add(new Couple<>("hold_perspective_front_hide_hud", true));
		configProvider.add(new Couple<>("hide_hud_hide_vignette", false));
		configProvider.add(new Couple<>("version_overlay", false));
		configProvider.add(new Couple<>("position_overlay", false));
		configProvider.add(new Couple<>("time_overlay", false));
		configProvider.add(new Couple<>("day_overlay", false));
		configProvider.add(new Couple<>("biome_overlay", false));
		configProvider.add(new Couple<>("cps_overlay", false));
	}

	protected static void assign() {
		zoomEnabled = config.getOrDefault("zoom_enabled", true);
		zoomLevel = config.getOrDefault("zoom_level", 40);
		zoomIncrementSize = config.getOrDefault("zoom_increment_size", 2);
		zoomTransition = config.getOrDefault("zoom_transition", "smooth");
		zoomSmoothSpeedIn = config.getOrDefault("zoom_smooth_speed_in", 1.0D);
		zoomSmoothSpeedOut = config.getOrDefault("zoom_smooth_speed_out", 1.0D);
		zoomScaleMode = config.getOrDefault("zoom_scale_mode", "scaled");
		zoomHideHud = config.getOrDefault("zoom_hide_hud", true);
		zoomShowPercentage = config.getOrDefault("zoom_show_percentage", false);
		zoomType = config.getOrDefault("zoom_type", "viewpoint:logarithmic");
		zoomReset = config.getOrDefault("zoom_reset", false);
		zoomCinematic = config.getOrDefault("zoom_cinematic", false);
		holdPerspectiveBackHideHud = config.getOrDefault("hold_perspective_back_hide_hud", true);
		holdPerspectiveFrontHideHud = config.getOrDefault("hold_perspective_front_hide_hud", true);
		holdPerspectiveBackMultiplier = config.getOrDefault("hold_perspective_back_multiplier", 1.0D);
		holdPerspectiveFrontMultiplier = config.getOrDefault("hold_perspective_front_multiplier", 1.0D);
		hideHudVignette = config.getOrDefault("hide_hud_hide_vignette", false);
		versionOverlay = config.getOrDefault("version_overlay", false);
		positionOverlay = config.getOrDefault("position_overlay", false);
		timeOverlay = config.getOrDefault("time_overlay", "false");
		dayOverlay = config.getOrDefault("day_overlay", false);
		biomeOverlay = config.getOrDefault("biome_overlay", false);
		cpsOverlay = config.getOrDefault("cps_overlay", false);
	}

	protected static void save() {
		Data.version.sendToLog(LogType.INFO,"Writing config to file.");
		configProvider.setConfig("zoom_enabled", zoomEnabled);
		configProvider.setConfig("zoom_level", zoomLevel);
		configProvider.setConfig("zoom_increment_size", zoomIncrementSize);
		configProvider.setConfig("zoom_transition", zoomTransition);
		configProvider.setConfig("zoom_smooth_speed_in", zoomSmoothSpeedIn);
		configProvider.setConfig("zoom_smooth_speed_out", zoomSmoothSpeedOut);
		configProvider.setConfig("zoom_scale_mode", zoomScaleMode);
		configProvider.setConfig("zoom_hide_hud", zoomHideHud);
		configProvider.setConfig("zoom_show_percentage", zoomShowPercentage);
		configProvider.setConfig("zoom_type", zoomType);
		configProvider.setConfig("zoom_reset", zoomReset);
		configProvider.setConfig("zoom_cinematic", zoomCinematic);
		configProvider.setConfig("hold_perspective_back_multiplier", holdPerspectiveBackMultiplier);
		configProvider.setConfig("hold_perspective_front_multiplier", holdPerspectiveFrontMultiplier);
		configProvider.setConfig("hold_perspective_back_hide_hud", holdPerspectiveBackHideHud);
		configProvider.setConfig("hold_perspective_front_hide_hud", holdPerspectiveFrontHideHud);
		configProvider.setConfig("hide_hud_hide_vignette", hideHudVignette);
		configProvider.setConfig("version_overlay", versionOverlay);
		configProvider.setConfig("position_overlay", positionOverlay);
		configProvider.setConfig("time_overlay", timeOverlay);
		configProvider.setConfig("day_overlay", dayOverlay);
		configProvider.setConfig("biome_overlay", biomeOverlay);
		configProvider.setConfig("cps_overlay", cpsOverlay);
		configProvider.saveConfig(Data.version, id);
	}
	static {
		options = new Object[]{
				zoomLevel,
				zoomIncrementSize,
				zoomTransition,
				zoomSmoothSpeedIn,
				zoomSmoothSpeedOut,
				zoomScaleMode,
				zoomHideHud,
				zoomShowPercentage,
				zoomType,
				zoomReset,
				zoomCinematic,
				holdPerspectiveBackMultiplier,
				holdPerspectiveFrontMultiplier,
				holdPerspectiveBackHideHud,
				holdPerspectiveFrontHideHud,
				hideHudVignette,
				versionOverlay,
				positionOverlay,
				timeOverlay,
				dayOverlay,
				biomeOverlay,
				cpsOverlay
		};
	}
}