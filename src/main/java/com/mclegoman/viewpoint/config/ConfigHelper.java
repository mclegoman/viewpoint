/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.config;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.ConfigScreen;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfigHelper {
	public static boolean showReloadOverlay = false;
	public static int showReloadOverlayTicks = 20;
	protected static final int saveViaTickSaveTick = 20;
	protected static boolean saveViaTick = false;
	protected static int saveViaTickTicks = 0;
	private static boolean savingConfig = false;
	private static boolean finishedInitializing = false;
	public static boolean isSaving() {
		return savingConfig;
	}
	public static boolean isFinishedInitializing() {
		return finishedInitializing;
	}
	public static void init() {
		try {
			reloadConfig(false);
			finishedInitializing = true;
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to initialize config!: {}", error));
		}
	}
	public static void reloadConfig(boolean log) {
		if (log) {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Reloading Config!"));
			showReloadOverlay = true;
			showReloadOverlayTicks = 20;
		}
		Config.init();
	}
	public static void tick() {
		try {
			if (Keybindings.openConfig.wasPressed())
				ClientData.minecraft.setScreen(new ConfigScreen(ClientData.minecraft.currentScreen, false, true, 1));
			if (saveViaTickTicks < saveViaTickSaveTick) saveViaTickTicks += 1;
			else {
				if (saveViaTick) {
					saveConfigs();
					saveViaTick = false;
					savingConfig = false;
				}
				saveViaTickTicks = 0;
			}
			if (showReloadOverlay) {
				if (showReloadOverlayTicks < 1) {
					showReloadOverlay = false;
					showReloadOverlayTicks = 20;
				} else {
					showReloadOverlayTicks -= 1;
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to tick config!");
		}
	}
	private static void saveConfigs() {
		try {
			savingConfig = true;
			Config.save();
			savingConfig = false;
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to save config!");
		}
	}
	public static void saveConfig() {
		saveViaTick = true;
	}
	public static boolean resetConfig() {
		List<Boolean> configChanged = new ArrayList<>();
		try {
			configChanged.add(setConfig("zoom_enabled", true));
			configChanged.add(setConfig("zoom_level", MathHelper.clamp(40, 0, 100)));
			configChanged.add(setConfig("zoom_increment_size", MathHelper.clamp(2, 1, 10)));
			configChanged.add(setConfig("zoom_transition", "smooth"));
			configChanged.add(setConfig("zoom_smooth_speed_in", MathHelper.clamp(1.0D, 0.001D, 2.0D)));
			configChanged.add(setConfig("zoom_smooth_speed_out", MathHelper.clamp(1.0D, 0.001D, 2.0D)));
			configChanged.add(setConfig("zoom_scale_mode", "scaled"));
			configChanged.add(setConfig("zoom_hide_hud", true));
			configChanged.add(setConfig("zoom_show_percentage", false));
			configChanged.add(setConfig("zoom_type", "viewpoint:logarithmic"));
			configChanged.add(setConfig("zoom_reset", false));
			configChanged.add(setConfig("zoom_cinematic", false));
			configChanged.add(setConfig("hold_perspective_back_multiplier", MathHelper.clamp(1.0D, 0.5D, 4.0D)));
			configChanged.add(setConfig("hold_perspective_front_multiplier", MathHelper.clamp(1.0D, 0.5D, 4.0D)));
			configChanged.add(setConfig("hold_perspective_back_hide_hud", true));
			configChanged.add(setConfig("hold_perspective_front_hide_hud", true));
			configChanged.add(setConfig("hide_hud_hide_vignette", false));
			configChanged.add(setConfig("version_overlay", false));
			configChanged.add(setConfig("position_overlay", false));
			configChanged.add(setConfig("time_overlay", "false"));
			configChanged.add(setConfig("day_overlay", false));
			configChanged.add(setConfig("biome_overlay", false));
			configChanged.add(setConfig("cps_overlay", false));
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to reset config!");
		}
		return configChanged.contains(true);
	}
	public static boolean setConfig(String key, Object value) {
		boolean configChanged = false;
		try {
			switch (key) {
				case "zoom_enabled" -> {
					Config.zoomEnabled = (boolean) value;
					configChanged = true;
				}
				case "zoom_level" -> {
					Config.zoomLevel = MathHelper.clamp((int) value, 0, 100);
					configChanged = true;
				}
				case "zoom_increment_size" -> {
					Config.zoomIncrementSize = MathHelper.clamp((int) value, 1, 10);
					configChanged = true;
				}
				case "zoom_transition" -> {
					Config.zoomTransition = (String) value;
					configChanged = true;
				}
				case "zoom_smooth_speed_in" -> {
					Config.zoomSmoothSpeedIn = MathHelper.clamp((double) value, 0.001D, 2.0D);
					configChanged = true;
				}
				case "zoom_smooth_speed_out" -> {
					Config.zoomSmoothSpeedOut = (double) value;
					configChanged = true;
				}
				case "zoom_scale_mode" -> {
					Config.zoomScaleMode = (String) value;
					configChanged = true;
				}
				case "zoom_hide_hud" -> {
					Config.zoomHideHud = (boolean) value;
					configChanged = true;
				}
				case "zoom_show_percentage" -> {
					Config.zoomShowPercentage = (boolean) value;
					configChanged = true;
				}
				case "zoom_type" -> {
					Config.zoomType = (String) value;
					configChanged = true;
				}
				case "zoom_reset" -> {
					Config.zoomReset = (boolean) value;
					configChanged = true;
				}
				case "zoom_cinematic" -> {
					Config.zoomCinematic = (boolean) value;
					configChanged = true;
				}
				case "hold_perspective_back_multiplier" -> {
					Config.holdPerspectiveBackMultiplier = MathHelper.clamp((double) value, 0.5D, 4.0D);
					configChanged = true;
				}
				case "hold_perspective_front_multiplier" -> {
					Config.holdPerspectiveFrontMultiplier = MathHelper.clamp((double) value, 0.5D, 4.0D);
					configChanged = true;
				}
				case "hold_perspective_back_hide_hud" -> {
					Config.holdPerspectiveBackHideHud = (boolean) value;
					configChanged = true;
				}
				case "hold_perspective_front_hide_hud" -> {
					Config.holdPerspectiveFrontHideHud = (boolean) value;
					configChanged = true;
				}
				case "hide_hud_hide_vignette" -> {
					Config.hideHudVignette = (boolean) value;
					configChanged = true;
				}
				case "version_overlay" -> {
					Config.versionOverlay = (boolean) value;
					configChanged = true;
				}
				case "position_overlay" -> {
					Config.positionOverlay = (boolean) value;
					configChanged = true;
				}
				case "time_overlay" -> {
					Config.timeOverlay = (String) value;
					configChanged = true;
				}
				case "day_overlay" -> {
					Config.dayOverlay = (boolean) value;
					configChanged = true;
				}
				case "biome_overlay" -> {
					Config.biomeOverlay = (boolean) value;
					configChanged = true;
				}
				case "cps_overlay" -> {
					Config.cpsOverlay = (boolean) value;
					configChanged = true;
				}
				default -> {
					Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set {} config value!: Invalid Key", key));
				}
			}
			if (configChanged) saveConfigs();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set {} config value!: {}", key, error));
		}
		return configChanged;
	}
	public static Object getConfig(String key) {
		switch (key) {
			case "zoom_enabled" -> {
				return Config.zoomEnabled;
			}
			case "zoom_level" -> {
				return MathHelper.clamp(Config.zoomLevel, 0, 100);
			}
			case "zoom_increment_size" -> {
				return MathHelper.clamp(Config.zoomIncrementSize, 1, 10);
			}
			case "zoom_transition" -> {
				return Config.zoomTransition;
			}
			case "zoom_smooth_speed_in" -> {
				return MathHelper.clamp(Config.zoomSmoothSpeedIn, 0.001D, 2.0D);
			}
			case "zoom_smooth_speed_out" -> {
				return MathHelper.clamp(Config.zoomSmoothSpeedOut, 0.001D, 2.0D);
			}
			case "zoom_scale_mode" -> {
				return Config.zoomScaleMode;
			}
			case "zoom_hide_hud" -> {
				return Config.zoomHideHud;
			}
			case "zoom_show_percentage" -> {
				return Config.zoomShowPercentage;
			}
			case "zoom_type" -> {
				return Config.zoomType;
			}
			case "zoom_reset" -> {
				return Config.zoomReset;
			}
			case "zoom_cinematic" -> {
				return Config.zoomCinematic;
			}
			case "hold_perspective_back_multiplier" -> {
				return MathHelper.clamp(Config.holdPerspectiveBackMultiplier, 0.5D, 4.0D);
			}
			case "hold_perspective_front_multiplier" -> {
				return MathHelper.clamp(Config.holdPerspectiveFrontMultiplier, 0.5D, 4.0D);
			}
			case "hold_perspective_back_hide_hud" -> {
				return Config.holdPerspectiveBackHideHud;
			}
			case "hold_perspective_front_hide_hud" -> {
				return Config.holdPerspectiveFrontHideHud;
			}
			case "hide_hud_hide_vignette" -> {
				return Config.hideHudVignette;
			}
			case "version_overlay" -> {
				return Config.versionOverlay;
			}
			case "position_overlay" -> {
				return Config.positionOverlay;
			}
			case "time_overlay" -> {
				return Config.timeOverlay;
			}
			case "day_overlay" -> {
				return Config.dayOverlay;
			}
			case "biome_overlay" -> {
				return Config.biomeOverlay;
			}
			case "cps_overlay" -> {
				return Config.cpsOverlay;
			}
			default -> {
				Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Key", key));
				return new Object();
			}
		}
	}
}