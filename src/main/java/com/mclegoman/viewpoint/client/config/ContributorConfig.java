package com.mclegoman.viewpoint.client.config;

import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.config.LuminanceConfigHelper;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.values.TrackedValue;

public class ContributorConfig extends ReflectiveConfig {
	public static final ContributorConfig config = LuminanceConfigHelper.register(Data.getVersion().getID(), "contributor", ContributorConfig.class);
	public final TrackedValue<Boolean> flip = this.value(false);
	public final TrackedValue<Boolean> flipOnHoldPerspectiveFront = this.value(false);
	public final TrackedValue<Boolean> flipOnHoldPerspectiveBack = this.value(false);
	public static void init() {}
	public static boolean isFlip() {
		if (Perspective.isHoldingPerspective()) {
			if (Perspective.isHoldingPerspectiveFront() && config.flipOnHoldPerspectiveFront.value()) return true;
			if (Perspective.isHoldingPerspectiveBack() && config.flipOnHoldPerspectiveBack.value()) return true;
		}
		return config.flip.value();
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