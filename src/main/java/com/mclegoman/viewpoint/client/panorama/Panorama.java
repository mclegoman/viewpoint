/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.LogType;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Panorama {
	private static final List<String> incompatibleMods = new ArrayList<>();

	public static void addIncompatibleMod(String modID) {
		if (!incompatibleMods.contains(modID)) incompatibleMods.add(modID);
	}

	public static List<String> getIncompatibleMods() {
		List<String> incompatibleModsFound = new ArrayList<>();
		for (String modID : incompatibleMods) {
			if (Data.isModInstalled(modID)) {
				incompatibleModsFound.add(Data.getModContainer(modID).getMetadata().getName());
			}
		}
		return incompatibleModsFound;
	}

	public static void init() {
		addIncompatibleMod("canvas");
		addIncompatibleMod("iris");
	}

	public static void tick() {
		if (Keybindings.takePanoScreenshot.wasPressed()) takePanorama(1024, 1024);
	}

	private static File getFile() {
		String currentTime = Util.getFormattedCurrentTime();
		int i = 1;
		boolean shouldReturn = false;
		File file = null;
		while (!shouldReturn) {
			String filename1 = currentTime + (i == 1 ? "" : "_" + i);
			file = new File(ClientData.minecraft.runDirectory.getPath() + "/panoramas/", filename1);
			if (!file.exists()) {
				shouldReturn = true;
			}
			i++;
		}
		return file;
	}

	private static boolean shouldTakePanorama() {
		return getIncompatibleMods().isEmpty() && !ClientData.minecraft.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS);
	}

	private static void takePanorama(int width, int height) {
		try {
			if (shouldTakePanorama()) {
				if (ClientData.minecraft.player != null) {
					final File file = getFile();
					final File screenshotFolder = new File(file.getPath() + "/screenshots/");
					if (screenshotFolder.mkdirs()) {
						for (int i = 0; i < 6; i++) {
							File imageFile = new File(screenshotFolder, "panorama_" + i + ".png");
							imageFile.createNewFile();
						}
						final float pitch = ClientData.minecraft.player.getPitch();
						final float yaw = ClientData.minecraft.player.getYaw();
						ClientData.minecraft.player.setYaw(0.0F);
						ClientData.minecraft.player.sendMessage(ClientData.minecraft.takePanorama(file, width, height), false);
						ClientData.minecraft.player.setPitch(pitch);
						ClientData.minecraft.player.setYaw(yaw);
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to take panoramic screenshot: {}", error));
		}
	}
}