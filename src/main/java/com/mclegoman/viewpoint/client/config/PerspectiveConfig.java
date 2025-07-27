/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.config;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.luminance.config.LuminanceConfigHelper;
import com.mclegoman.viewpoint.client.config.value.ConfigIdentifier;
import com.mclegoman.viewpoint.client.config.value.QualityToggle;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.screen.config.ConfigScreen;
import com.mclegoman.viewpoint.client.toasts.PerspectiveToast;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.common.util.Identifiers;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.annotations.IntegerRange;
import org.quiltmc.config.api.annotations.SerializedName;
import org.quiltmc.config.api.values.TrackedValue;

public class PerspectiveConfig extends ReflectiveConfig {
	public static final PerspectiveConfig config = LuminanceConfigHelper.register(Data.getVersion().getID(), "config", PerspectiveConfig.class);
	@SerializedName("zoom_enabled")
	public final TrackedValue<Boolean> zoomEnabled = this.value(true);
	@SerializedName("zoom_level")
	@FloatRange(min = 0.0F, max = 100.0F)
	public final TrackedValue<Float> zoomLevel = this.value(40.0F);
	@SerializedName("zoom_increment_size")
	@FloatRange(min = 0.1F, max = 10.0F)
	public final TrackedValue<Float> zoomIncrementSize = this.value(2.0F);
	@SerializedName("zoom_transition")
	public final TrackedValue<String> zoomTransition = this.value("smooth");
	@SerializedName("zoom_smooth_speed_in")
	@FloatRange(min = 0, max = 2)
	public final TrackedValue<Float> zoomSmoothSpeedIn = this.value(1.0F);
	@SerializedName("zoom_smooth_speed_out")
	@FloatRange(min = 0, max = 2)
	public final TrackedValue<Float> zoomSmoothSpeedOut = this.value(1.0F);
	@SerializedName("zoom_scale_mode")
	public final TrackedValue<String> zoomScaleMode = this.value("scaled");
	@SerializedName("zoom_hide_hud")
	public final TrackedValue<String> zoomHideHud = this.value("false");
	@SerializedName("zoom_show_percentage")
	public final TrackedValue<Boolean> zoomShowPercentage = this.value(false);
	@SerializedName("zoom_type")
	public final TrackedValue<ConfigIdentifier> zoomType = this.value(ConfigIdentifier.of(Identifiers.LOGARITHMIC));
	@SerializedName("zoom_reset")
	public final TrackedValue<Boolean> zoomReset = this.value(false);
	@SerializedName("zoom_cinematic")
	public final TrackedValue<Boolean> zoomCinematic = this.value(false);
	@SerializedName("hold_perspective_multiplier_increment_size")
	@IntegerRange(min = 1, max = 10)
	public final TrackedValue<Integer> holdPerspectiveMultiplierIncrementSize = this.value(5);
	@SerializedName("perspective_multiplier")
	public final TrackedValue<Boolean> perspectiveMultiplier = this.value(false);
	@SerializedName("hold_perspective_back_multiplier")
	@FloatRange(min = 0.5, max = 16)
	public final TrackedValue<Float> holdPerspectiveBackMultiplier = this.value(1.0F);
	@SerializedName("hold_perspective_front_multiplier")
	@FloatRange(min = 0.5, max = 16)
	public final TrackedValue<Float> holdPerspectiveFrontMultiplier = this.value(1.0F);
	@FloatRange(min = 0.5, max = 16)
	public final TrackedValue<Float> holdPerspectiveTopMultiplier = this.value(4.0F);
	@SerializedName("hold_perspective_back_hide_hud")
	public final TrackedValue<Boolean> holdPerspectiveBackHideHud = this.value(false);
	@SerializedName("hold_perspective_front_hide_hud")
	public final TrackedValue<Boolean> holdPerspectiveFrontHideHud = this.value(true);
	@SerializedName("hold_perspective_top_hide_hud")
	public final TrackedValue<Boolean> holdPerspectiveTopHideHud = this.value(true);
	@SerializedName("star_brightness_multiplier")
	@FloatRange(min = 0.0F, max = 2.0F)
	public final TrackedValue<Float> starBrightnessMultiplier = this.value(1.0F);
	@SerializedName("textured_named_entity")
	public final TrackedValue<Boolean> texturedNamedEntity = this.value(true);
	@SerializedName("textured_random_entity")
	public final TrackedValue<Boolean> texturedRandomEntity = this.value(false);
	@SerializedName("allow_april_fools")
	public final TrackedValue<Boolean> allowAprilFools = this.value(true);
	@SerializedName("force_april_fools")
	public final TrackedValue<Boolean> forceAprilFools = this.value(false);
	@SerializedName("allow_halloween")
	public final TrackedValue<Boolean> allowHalloween = this.value(true);
	@SerializedName("force_halloween")
	public final TrackedValue<Boolean> forceHalloween = this.value(false);
	@SerializedName("version_overlay")
	public final TrackedValue<Boolean> versionOverlay = this.value(false);
	@SerializedName("position_overlay")
	public final TrackedValue<Boolean> positionOverlay = this.value(false);
	@SerializedName("time_overlay")
	public final TrackedValue<String> timeOverlay = this.value("false");
	@SerializedName("day_overlay")
	public final TrackedValue<Boolean> dayOverlay = this.value(false);
	@SerializedName("biome_overlay")
	public final TrackedValue<Boolean> biomeOverlay = this.value(false);
	@SerializedName("looking_at_overlay")
	public final TrackedValue<QualityToggle> lookingAtOverlay = this.value(QualityToggle.off);
	@SerializedName("cps_overlay")
	public final TrackedValue<Boolean> cpsOverlay = this.value(false);
	@SerializedName("deaths_overlay")
	public final TrackedValue<Boolean> deathsOverlay = this.value(false);
	@SerializedName("totems_overlay")
	public final TrackedValue<Boolean> totemsOverlay = this.value(false);
	@SerializedName("armor_overlay")
	public final TrackedValue<Boolean> armorOverlay = this.value(false);
	@SerializedName("force_pride")
	public final TrackedValue<Boolean> forcePride = this.value(false);
	@SerializedName("force_pride_type")
	public final TrackedValue<String> forcePrideType = this.value("random");
	@SerializedName("show_death_coordinates")
	public final TrackedValue<Boolean> showDeathCoordinates = this.value(false);
	@SerializedName("ui_background")
	public final TrackedValue<ConfigIdentifier> uiBackground = this.value(ConfigIdentifier.of(Identifiers.DEFAULT));
	@SerializedName("ui_background_texture")
	public final TrackedValue<ConfigIdentifier> uiBackgroundTexture = this.value(ConfigIdentifier.of("minecraft:block/dirt"));
	@SerializedName("crosshair_type")
	public final TrackedValue<String> crosshairType = this.value("vanilla");
	@SerializedName("hide_block_outline")
	public final TrackedValue<Boolean> hideBlockOutline = this.value(false);
	@SerializedName("block_outline")
	public final TrackedValue<Integer> blockOutline = this.value(40);
	@SerializedName("rainbow_block_outline")
	public final TrackedValue<Boolean> rainbowBlockOutline = this.value(false);
	@SerializedName("hide_armor")
	public final TrackedValue<Boolean> hideArmor = this.value(false);
	@SerializedName("hide_nametags")
	public final TrackedValue<Boolean> hideNametags = this.value(false);
	@SerializedName("hide_players")
	public final TrackedValue<Boolean> hidePlayers = this.value(false);
	@SerializedName("hide_show_message")
	public final TrackedValue<Boolean> hideShowMessage = this.value(true);
	@SerializedName("detect_update_channel")
	public final TrackedValue<String> detectUpdateChannel = this.value("release");
	@SerializedName("tutorials")
	public final TrackedValue<Boolean> tutorials = this.value(true);
	@SerializedName("debug")
	public final TrackedValue<Boolean> debug = this.value(false);
	@SerializedName("panorama_resolution")
	public final TrackedValue<Integer> panoramaResolution = this.value(1024);
	@SerializedName("bounce_panorama_yaw")
	public final TrackedValue<Boolean> bouncePanoramaYaw = this.value(false);
	@SerializedName("bounce_panorama_yaw_angle")
	public final TrackedValue<Float> bouncePanoramaYawAngle = this.value(16.0F);
	@SerializedName("bounce_panorama_yaw_speed")
	public final TrackedValue<Float> bouncePanoramaYawSpeed = this.value(0.005F);
	@SerializedName("panorama_pitch_speed")
	public final TrackedValue<Float> panoramaPitchSpeed = this.value(0.1F);
	@SerializedName("merge_xp_and_locator_bar")
	public final TrackedValue<Boolean> mergeXPLocatorBar = this.value(false);
	@SerializedName("config_version")
	@Comment("Do not edit this! This is used for updating the config.")
	public final TrackedValue<Float> configVersion = this.value(ClientData.configVersion);
	public static void init() {
		try {
			if (Contributor.isClientContributor()) ContributorConfig.init();
			update();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.WARN, "Failed to init config!");
		}
	}
	public static void update() {
		try {
			final float configVersion = config.configVersion.value();
			if (configVersion != ClientData.configVersion) {
				if (configVersion < ClientData.configVersion) {
					// Config versions under 24 cannot be upgraded.
				} PerspectiveToast.Helper.showDowngradeWarning();
				config.configVersion.setValue(ClientData.configVersion, true);
			}
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.WARN, "Failed to update config!");
		}
	}
	public static void tick() {
		try {
			if (Keybindings.openConfig.wasPressed()) ClientData.minecraft.setScreen(new ConfigScreen(ClientData.minecraft.currentScreen, 1));
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.WARN, "Failed to tick config!");
		}
	}
	public static void toggleConfigValue(TrackedValue<Boolean> value) {
		config.toggle(value, true);
	}
	public static void toggleConfigValue(TrackedValue<Boolean> value, boolean shouldSave) {
		config.toggle(value, shouldSave);
	}
	public void toggle(TrackedValue<Boolean> value) {
		toggle(value, true);
	}
	public void toggle(TrackedValue<Boolean> value, boolean shouldSave) {
		value.setValue(!value.value(), shouldSave);
		if (shouldSave) config.save();
	}
	public void reset() {
		reset(true);
	}
	public void reset(boolean shouldSave) {
		for (TrackedValue value : config.values()) value.setValue(value.getDefaultValue(), false);
		if (shouldSave) config.save();
	}
}