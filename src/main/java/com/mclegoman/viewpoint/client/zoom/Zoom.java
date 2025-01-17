/*
    viewpoint
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/viewpoint
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.zoom;

import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.IdentifierHelper;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Smoother;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zoom {
	public static final List<Identifier> zoomTypes = new ArrayList<>();
	public static final String[] zoomTransitions = new String[]{"smooth", "instant"};
	public static final String[] zoomScaleModes = new String[]{"scaled", "vanilla"};
	private static boolean isZooming;
	private static boolean hasUpdated;
	private static double prevMultiplier = 1.0D;
	private static double multiplier = 1.0D;
	public static float fov = 70.0F;
	public static float zoomFOV = 70.0F;
	public static double timeDelta = Double.MIN_VALUE;
	public static Smoother smoothX = new Smoother();
	public static Smoother smoothY = new Smoother();
	public static void addZoomType(Identifier identifier) {
		if (!zoomTypes.contains(identifier)) zoomTypes.add(identifier);
	}
	public static void init() {
		try {
			addZoomType(Logarithmic.getIdentifier());
			addZoomType(Linear.getIdentifier());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to init zoom: {}", error));
		}
	}
	public static void tick() {
		try {
			if (Keybindings.toggleZoom.wasPressed()) isZooming = !isZooming;
			if (Keybindings.toggleZoomCinematic.wasPressed()) {
				resetCinematicZoom();
				ConfigHelper.setConfig("zoom_cinematic", !(boolean) ConfigHelper.getConfig("zoom_cinematic"));
			}
			if (!isZooming()) {
				if ((boolean) ConfigHelper.getConfig("zoom_reset")) {
					if (getRawZoomLevel() != getDefaultZoomLevel()) {
						reset();
						hasUpdated = true;
					}
				}
				if (hasUpdated) {
					ConfigHelper.saveConfig();
					hasUpdated = false;
				}
				resetCinematicZoom();
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick zoom: {}", error));
		}
	}
	public static void resetCinematicZoom() {
		smoothX = new Smoother();
		smoothY = new Smoother();
	}
	public static double getMouseSensitivity() {
		return Math.pow(ClientData.minecraft.options.getMouseSensitivity().getValue() * 0.6000000238418579F + 0.20000000298023224F, 3.0F) * 8.0F;
	}
	public static boolean isZooming() {
		return canZoom() && ClientData.minecraft.player != null && (isZooming != Keybindings.holdZoom.isPressed());
	}
	public static boolean canZoom() {
		return ClientData.minecraft.cameraEntity != null && (boolean)ConfigHelper.getConfig("zoom_enabled");
	}
	public static boolean isScaled() {
		return ConfigHelper.getConfig("zoom_scale_mode").equals("scaled");
	}
	public static boolean isSmoothCamera() {
		return (boolean) ConfigHelper.getConfig("zoom_cinematic");
	}
	public static void updateMultiplier() {
		try {
			prevMultiplier = multiplier;
			if (!isZooming()) Multiplier.setMultiplier(1.0F);
			else {
				if (getZoomType().equals(Logarithmic.getIdentifier())) Logarithmic.updateMultiplier();
				if (getZoomType().equals(Linear.getIdentifier())) Linear.updateMultiplier();
			}
			multiplier = Multiplier.getMultiplier();
			updateTransition();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to update zoom multiplier: {}", error));
		}
	}
	public static void updateTransition() {
		try {
			if ((ConfigHelper.getConfig("zoom_transition")).equals("smooth")) {
				double speedMultiplier = ((prevMultiplier + multiplier) * 0.5);
				multiplier = MathHelper.lerp((prevMultiplier < speedMultiplier) ? (double)ConfigHelper.getConfig("zoom_smooth_speed_out") : (double)ConfigHelper.getConfig("zoom_smooth_speed_in"), prevMultiplier, speedMultiplier);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to update zoom transition: {}", error));
		}
	}
	public static double getPrevMultiplier() {
		return prevMultiplier;
	}
	public static double getMultiplier() {
		return multiplier;
	}
	public static double getMultiplierFromFOV() {
		return zoomFOV/fov;
	}
	public static Identifier getZoomType() {
		Identifier zoomTypeIdentifier = IdentifierHelper.identifierFromString((String) ConfigHelper.getConfig("zoom_type"));
		while (!isValidZoomType(zoomTypeIdentifier)) zoomTypeIdentifier = IdentifierHelper.identifierFromString(cycleZoomType());
		return zoomTypeIdentifier;
	}
	public static float getZoomLevel() {
		return MathHelper.clamp(getRawZoomLevel(), 0.0F, 100.0F);
	}
	public static int getRawZoomLevel() {
		return (int) ConfigHelper.getConfig("zoom_level");
	}
	public static int getDefaultZoomLevel() {
		return 40;
	}
	public static void zoom(int amount, int multiplier) {
		try {
			boolean updated = false;
			for (int i = 0; i < multiplier; i++) {
				if (!(getRawZoomLevel() <= 0) || !(getRawZoomLevel() >= 100)) {
					ConfigHelper.setConfig("zoom_level", getRawZoomLevel() + amount);
					updated = true;
					hasUpdated = true;
				}
			}
			if (updated) setOverlay();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set zoom level: {}", error));
		}
	}
	public static void reset() {
		try {
			if ((int) ConfigHelper.getConfig("zoom_level") != 40) {
				ConfigHelper.setConfig("zoom_level", 40);
				setOverlay();
				hasUpdated = true;
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset zoom level: {}", error));
		}
	}
	private static void setOverlay() {
		try {
			if ((boolean) ConfigHelper.getConfig("zoom_show_percentage"))
				if (ClientData.minecraft.player != null) {
					ClientData.minecraft.player.sendMessage((Text.translatable("gui.viewpoint.message.zoom_level", Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD)), true);
				}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set zoom overlay: {}", error));
		}
	}
	public static String cycleZoomType() {
		return cycleZoomType(true);
	}
	public static String cycleZoomType(boolean direction) {
		try {
			int currentIndex = isValidZoomType(getZoomType()) ? zoomTypes.indexOf(getZoomType()) : 0;
			String zoomType = IdentifierHelper.stringFromIdentifier(zoomTypes.get(direction ? (currentIndex + 1) % zoomTypes.size() : (currentIndex - 1 + zoomTypes.size()) % zoomTypes.size()));
			ConfigHelper.setConfig("zoom_type", zoomType);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to cycle zoom type: {}", error));
		}
		return null;
	}
	public static boolean isValidZoomType(Identifier zoomType) {
		return zoomTypes.contains(zoomType);
	}
	public static String nextTransition() {
		List<String> transitions = Arrays.stream(zoomTransitions).toList();
		return transitions.contains((String) ConfigHelper.getConfig("zoom_transition")) ? zoomTransitions[(transitions.indexOf((String) ConfigHelper.getConfig("zoom_transition")) + 1) % zoomTransitions.length] : zoomTransitions[0];
	}
	public static String nextScaleMode() {
		List<String> scaleModes = Arrays.stream(zoomScaleModes).toList();
		return scaleModes.contains((String) ConfigHelper.getConfig("zoom_scale_mode")) ? zoomScaleModes[(scaleModes.indexOf((String) ConfigHelper.getConfig("zoom_scale_mode")) + 1) % zoomScaleModes.length] : zoomScaleModes[0];
	}
	public static class Logarithmic {
		public static Identifier getIdentifier() {
			return Identifier.of(Data.version.getID(), "logarithmic");
		}
		public static float getLimitFOV(double input) {
			return (float) MathHelper.clamp(input, 0.1, 179.9);
		}
		public static void updateMultiplier() {
			Multiplier.setMultiplier((float) (1.0F - (Math.log(Zoom.getZoomLevel() + 1.0F) / Math.log(100.0 + 1.0F))));
		}
	}
	public static class Linear {
		public static Identifier getIdentifier() {
			return Identifier.of(Data.version.getID(), "linear");
		}
		public static float getLimitFOV(double input) {
			return (float) MathHelper.clamp(input, 0.1, 179.9);
		}
		public static void updateMultiplier() {
			Multiplier.setMultiplier(1.0F - (Zoom.getZoomLevel() / 100.0F));
		}
	}
	public static class Multiplier {
		protected static float currentMultiplier = 1.0F;
		protected static float getMultiplier() {
			return currentMultiplier;
		}
		protected static void setMultiplier(float multiplier) {
			try {
				currentMultiplier = multiplier;
			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set Zoom Multiplier: {}", error));
			}
		}
	}
}