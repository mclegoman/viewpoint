/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hide;


import com.mclegoman.viewpoint.luminance.client.util.MessageOverlay;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.events.PerspectiveEvents;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.perspective.Perspective;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.common.util.Identifiers;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Hide {
	public static final String[] zoomHideHudModes = new String[]{"true", "hand", "false"};
	public static final String[] hideCrosshairModes = new String[]{"vanilla", "dynamic", "hidden"};
	public static float rainbowTime = 0.0F;
	public static void init() {
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.HIDE_ARMOR, new HideArmorDataLoader());
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.HIDE_NAME_TAGS, new HideNameTagsDataLoader());
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.HIDE_PLAYER, new HidePlayerDataLoader());
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.DYNAMIC_CROSSHAIR, new DynamicCrosshairDataLoader());
	}
	public static void tick() {
		if (Keybindings.toggleArmour.wasPressed()) {
			PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideArmor, true);
			if (PerspectiveConfig.config.hideShowMessage.value())
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.armor", Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hideArmor.value(), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleBlockOutline.wasPressed()) {
			PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideBlockOutline, true);
			if (PerspectiveConfig.config.hideShowMessage.value())
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.block_outline", Translation.getVariableTranslation(Data.getVersion().getID(), !PerspectiveConfig.config.hideBlockOutline.value(), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.cycleCrosshair.wasPressed()) {
			PerspectiveConfig.config.crosshairType.setValue(nextCrosshairMode(), true);
			if (PerspectiveConfig.config.hideShowMessage.value())
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.crosshair", Translation.getCrosshairTranslation(Data.getVersion().getID(), PerspectiveConfig.config.crosshairType.value())).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleNametags.wasPressed()) {
			PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideNametags, true);
			if (PerspectiveConfig.config.hideShowMessage.value())
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.nametags", Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hideNametags.value(), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.togglePlayers.wasPressed()) {
			PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hidePlayers, true);
			if (PerspectiveConfig.config.hideShowMessage.value())
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.players", Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hidePlayers.value(), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		rainbowTime += 1.0F % 20.0F;
	}
	public static boolean shouldHidePlayer(UUID uuid) {
		if (ClientData.minecraft.player != null) {
			if (!uuid.equals(ClientData.minecraft.player.getGameProfile().getId()))
				return PerspectiveConfig.config.hidePlayers.value() || HidePlayerDataLoader.registry.contains(String.valueOf(uuid));
		}
		return false;
	}
	public static String nextZoomHideHudMode() {
		List<String> modes = Arrays.stream(zoomHideHudModes).toList();
		return modes.contains(PerspectiveConfig.config.zoomHideHud.value()) ? zoomHideHudModes[(modes.indexOf(PerspectiveConfig.config.zoomHideHud.value()) + 1) % zoomHideHudModes.length] : zoomHideHudModes[0];
	}
	public static String nextCrosshairMode() {
		List<String> modes = Arrays.stream(hideCrosshairModes).toList();
		return modes.contains(PerspectiveConfig.config.crosshairType.value()) ? hideCrosshairModes[(modes.indexOf(PerspectiveConfig.config.crosshairType.value()) + 1) % hideCrosshairModes.length] : hideCrosshairModes[0];
	}
	public static boolean shouldHideHand(HideHudTypes type) {
		if (type == HideHudTypes.zoom) return Zoom.isZooming() && PerspectiveConfig.config.zoomHideHud.value().equalsIgnoreCase("hand");
		return false;
	}
	public static boolean shouldHideHud(HideHudTypes type) {
		switch (type) {
			case zoom -> {
				return Zoom.isZooming() && PerspectiveConfig.config.zoomHideHud.value().equalsIgnoreCase("true");
			}
			case holdPerspectiveBack -> {
				return Perspective.isHoldingPerspectiveBack() && PerspectiveConfig.config.holdPerspectiveBackHideHud.value();
			}
			case holdPerspectiveFront -> {
				return Perspective.isHoldingPerspectiveFront() && PerspectiveConfig.config.holdPerspectiveFrontHideHud.value();
			}
			default -> {return false;}
		}
	}
	public static boolean shouldHideArmor(UUID uuid) {
		return PerspectiveConfig.config.hideArmor.value() || HideArmorDataLoader.registry.contains(String.valueOf(uuid));
	}
	public static int getBlockOutlineLevel() {
		return PerspectiveConfig.config.blockOutline.value();
	}
	public static boolean getRainbowBlockOutline() {
		return PerspectiveConfig.config.rainbowBlockOutline.value();
	}
	public static int getRainbowOutline() {
		return Color.getHSBColor(rainbowTime / 20.0F, 1.0F, 1.0F).getRGB();
	}
	public static int getARGB(int color, int alpha) {
		return (Math.clamp(alpha, 0, 255) << 24) | (color & 0x00FFFFFF);
	}
}