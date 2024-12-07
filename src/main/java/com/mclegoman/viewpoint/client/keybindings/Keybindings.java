/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.keybindings;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
	public static final KeyBinding holdPerspectiveThirdPersonBack;
	public static final KeyBinding holdPerspectiveThirdPersonFront;
	public static final KeyBinding holdZoom;
	public static final KeyBinding openConfig;
	public static final KeyBinding setPerspectiveFirstPerson;
	public static final KeyBinding setPerspectiveThirdPersonBack;
	public static final KeyBinding setPerspectiveThirdPersonFront;
	public static final KeyBinding toggleZoom;
	public static final KeyBinding toggleZoomCinematic;
	public static final KeyBinding takePanoScreenshot;
	public static final KeyBinding toggleVerOverlay;
	public static final KeyBinding togglePosOverlay;
	public static final KeyBinding toggleDayOverlay;
	public static final KeyBinding toggleBiomeOverlay;
	public static final KeyBinding toggleCPSOverlay;
	public static final KeyBinding[] allKeybindings;

	static {
		allKeybindings = new KeyBinding[]{
				holdPerspectiveThirdPersonBack = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z),
				holdPerspectiveThirdPersonFront = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_perspective_third_person_front", GLFW.GLFW_KEY_V),
				holdZoom = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_zoom", GLFW.GLFW_KEY_R),
				openConfig = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "open_config", GLFW.GLFW_KEY_END),
				setPerspectiveFirstPerson = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_first_person", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonBack = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_third_person_back", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonFront = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_third_person_front", GLFW.GLFW_KEY_UNKNOWN),
				toggleZoom = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_zoom", GLFW.GLFW_KEY_UNKNOWN),
				toggleZoomCinematic = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_zoom_cinematic", GLFW.GLFW_KEY_UNKNOWN),
				takePanoScreenshot = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "take_pano_screenshot", GLFW.GLFW_KEY_UNKNOWN),
				toggleVerOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_version_overlay", GLFW.GLFW_KEY_UNKNOWN),
				togglePosOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_position_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleDayOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_day_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleBiomeOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_biome_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleCPSOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_cps_overlay", GLFW.GLFW_KEY_UNKNOWN)
		};
	}
	public static void init() {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing keybindings!"));
	}
	public static void tick() {
		if (!KeybindingHelper.seenConflictingKeybindingToasts) {
			if (KeybindingHelper.hasKeybindingConflicts()) {
				Data.version.sendToLog(LogType.INFO, Translation.getString("Conflicting Keybinding. Keybinding conflicts have been detected that could affect Perspective. Please take a moment to review and adjust your keybindings as needed."));
				ClientData.minecraft.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.keybinding_conflicts.title")}), Translation.getTranslation(Data.version.getID(), "toasts.keybinding_conflicts.description")));
			}
			KeybindingHelper.seenConflictingKeybindingToasts = true;
		}
	}
}