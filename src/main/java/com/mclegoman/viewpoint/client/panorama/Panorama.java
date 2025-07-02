/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.io.File;
import java.io.FileWriter;

public class Panorama {
	public static void tick() {
		if (Keybindings.takePanoScreenshot.wasPressed()) takePanorama(PerspectiveConfig.config.panoramaResolution.value(), 4);
	}
	private static String getFileName() {
		return getFileName(Util.getFormattedCurrentTime());
	}
	private static String getFileName(String fileName) {
		int i = 1;
		boolean shouldReturn = false;
		while (!shouldReturn) {
			String fileName1 = fileName + (i == 1 ? "" : "_" + i);
			File file = new File(ClientData.minecraft.runDirectory.getPath() + "/resourcepacks/", fileName1);
			if (!file.exists()) {
				fileName = fileName1;
				shouldReturn = true;
			}
			i++;
		}
		return fileName;
	}
	public static void takePanorama(int resolution, int scaleFactor) {
		String fileName = getFileName();
		takePanorama(fileName, fileName, resolution, scaleFactor);
	}
	public static void takePanorama(String resourcePackName, int resolution, int scaleFactor) {
		takePanorama(resourcePackName, resourcePackName, resolution, scaleFactor);
	}
	public static void takePanorama(String resourcePackName, String description, int resolution, int scaleFactor) {
		String panoramaName = getFileName(resourcePackName);
		int scaledResolution = resolution * scaleFactor;
		if (ClientData.minecraft.player != null) {
			int prevWidth = ClientData.minecraft.getWindow().getFramebufferWidth();
			int prevHeight = ClientData.minecraft.getWindow().getFramebufferHeight();
			Framebuffer framebuffer = ClientData.minecraft.getFramebuffer();
			float prevPitch = ClientData.minecraft.player.getPitch();
			float prevYaw = ClientData.minecraft.player.getYaw();
			float prevLastPitch = ClientData.minecraft.player.lastPitch;
			float prevLastYaw = ClientData.minecraft.player.lastYaw;
			ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(false);
			Perspective playerPerspective = ClientData.minecraft.options.getPerspective();
			if (!playerPerspective.isFirstPerson())
				ClientData.minecraft.options.setPerspective(Perspective.FIRST_PERSON);
			float startingYaw = findClosest(prevYaw);
			try {
				File resourcePackDir = new File(ClientData.minecraft.runDirectory.getPath() + "/resourcepacks/" + panoramaName);
				File screenshotsDir = new File(resourcePackDir + "/assets/minecraft/textures/gui/title/background");
				if (screenshotsDir.mkdirs()) {
					ClientData.minecraft.gameRenderer.setRenderingPanorama(true);
					resize(framebuffer, scaledResolution);

					for (int l = 0; l < 6; ++l) {
						switch (l) {
							case 0 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(0.0F);
							}
							case 1 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 90.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(0.0F);
							}
							case 2 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 180.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(0.0F);
							}
							case 3 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 270.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(0.0F);
							}
							case 4 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(-90.0F);
							}
							case 5 -> {
								ClientData.minecraft.player.setYaw((startingYaw + 0.0F) % 360.0F);
								ClientData.minecraft.player.setPitch(90.0F);
							}
						}
						save(screenshotsDir, "panorama_" + l + ".png", framebuffer, scaleFactor, false);
					}

					// Create pack.mcmeta
					File packFile = new File(resourcePackDir + "/pack.mcmeta");
					if (packFile.createNewFile()) {
						FileWriter packWriter = new FileWriter(packFile);
						packWriter.write("{\"pack\": {\"pack_format\": " + SharedConstants.getGameVersion().packVersion(ResourceType.CLIENT_RESOURCES) + ", \"supported_formats\": {\"min_inclusive\": 1, \"max_inclusive\": 2147483647}, \"description\": \"" + description + "\"}}\"}}");
						packWriter.close();
					}

					// Create pack.png
					ClientData.minecraft.player.setPitch(prevPitch);
					ClientData.minecraft.player.setYaw(prevYaw);
					resize(framebuffer, 64 * scaleFactor);
					save(resourcePackDir, "pack.png", framebuffer, scaleFactor, true);

					ClientData.minecraft.player.sendMessage(Translation.getTranslation(Data.getVersion().getID(), "message.take_panorama_screenshot.success", new Object[]{Text.literal(panoramaName).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent.OpenFile(resourcePackDir.getAbsolutePath())))}), false);
				}
			} catch (Exception error) {
				ClientData.minecraft.player.sendMessage(Translation.getTranslation(Data.getVersion().getID(), "message.take_panorama_screenshot.fail", new Object[]{error.getMessage()}, new Formatting[]{Formatting.RED}), false);
			} finally {
				ClientData.minecraft.player.setPitch(prevPitch);
				ClientData.minecraft.player.setYaw(prevYaw);
				ClientData.minecraft.player.lastPitch = prevLastPitch;
				ClientData.minecraft.player.lastYaw = prevLastYaw;
				ClientData.minecraft.gameRenderer.setBlockOutlineEnabled(true);
				ClientData.minecraft.getWindow().setFramebufferWidth(prevWidth);
				ClientData.minecraft.getWindow().setFramebufferHeight(prevHeight);
				framebuffer.resize(prevWidth, prevHeight);
				ClientData.minecraft.gameRenderer.setRenderingPanorama(false);
				ClientData.minecraft.options.setPerspective(playerPerspective);
			}
		}
	}
	private static float findClosest(float value) {
		float[] values = new float[]{0.0F, 90.0F, 180.0F, 270.0F};
		float closest = values[0];
		float minDiff = MathHelper.abs(value - closest);
		for (int i = 1; i < values.length; i++) {
			float diff = MathHelper.abs(value - values[i]);
			if (diff < minDiff) {
				minDiff = diff;
				closest = values[i];
			}
		}
		return closest;
	}
	private static void resize(Framebuffer framebuffer, int resolution) {
		ClientData.minecraft.getWindow().setFramebufferWidth(resolution);
		ClientData.minecraft.getWindow().setFramebufferHeight(resolution);
		framebuffer.resize(resolution, resolution);
	}
	private static void save(File dir, String fileName, Framebuffer framebuffer, int scaleFactor, boolean overlay) {
		ClientData.minecraft.gameRenderer.renderWorld(RenderTickCounter.ONE);
		ScreenshotRecorder.saveScreenshot(dir, fileName, framebuffer, scaleFactor, overlay);
	}
}