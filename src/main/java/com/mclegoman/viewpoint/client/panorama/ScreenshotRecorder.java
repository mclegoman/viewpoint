/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.logo.PerspectiveLogo;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ScreenshotRecorder {
	public static void saveScreenshot(File directory, String fileName, Framebuffer framebuffer, int downscaleFactor) {
		saveScreenshot(directory, fileName, framebuffer, downscaleFactor, false);
	}
	public static void saveScreenshot(File directory, String fileName, Framebuffer framebuffer, int downscaleFactor, boolean overlay) {
		net.minecraft.client.util.ScreenshotRecorder.takeScreenshot(framebuffer, downscaleFactor, (image) -> {
			directory.mkdirs();
			File file = new File(directory, fileName);
			Util.getIoWorkerExecutor().execute(() -> {
				try {
					if (overlay) {
						try {
							addOverlay(image, Identifier.of(Data.getVersion().getID(), "textures/panorama/overlay.png"), 1);
						} catch (Exception error) {
							Data.getVersion().sendToLog(LogType.ERROR, "Couldn't add overlay: " + error);
						}
					}
					image.writeTo(file);
					image.close();
				} catch (Exception error) {
					Data.getVersion().sendToLog(LogType.ERROR, "Couldn't save screenshot: " + error);
				}
			});
		});
	}
	private static void addOverlay(NativeImage image, Identifier texture, int factor) throws IOException {
		Optional<Resource> resource = ClientData.minecraft.getResourceManager().getResource(texture);
		if (resource.isEmpty()) {
			Data.getVersion().sendToLog(LogType.ERROR, "Couldn't find overlay texture with id '" + texture.toString() + "'!");
		} else {
			overlay(image, NativeImage.read(resource.orElseThrow().getInputStream()), factor);
		}
	}
	private static void overlay(NativeImage image, NativeImage overlayImage, int factor) {
		NativeImage overlay = downscaleByFactor(overlayImage, factor);
		for (int y = 0; y < Math.min(image.getHeight(), overlay.getHeight()); y++) {
			for (int x = 0; x < Math.min(image.getWidth(), overlay.getWidth()); x++) {
				image.setColorArgb(x, y, blend(image.getColorArgb(x, y), overlay.getColorArgb(x, y)));
			}
		}
	}
	private static int blend(int input, int overlay) {
		int inAlpha = (input >> 24) & 0xFF;
		int inRed = (input >> 16) & 0xFF;
		int inGreen = (input >> 8) & 0xFF;
		int inBlue = input & 0xFF;
		int overlayAlpha = (overlay >> 24) & 0xFF;
		int overlayRed = (overlay >> 16) & 0xFF;
		int overlayGreen = (overlay >> 8) & 0xFF;
		int overlayBlue = overlay & 0xFF;
		float alpha = overlayAlpha / 255.0F;
		int outRed = (int) ((1 - alpha) * inRed + alpha * overlayRed);
		int outGreen = (int) ((1 - alpha) * inGreen + alpha * overlayGreen);
		int outBlue = (int) ((1 - alpha) * inBlue + alpha * overlayBlue);
		int outAlpha = Math.max(inAlpha, overlayAlpha);
		return (outAlpha << 24) | (outRed << 16) | (outGreen << 8) | outBlue;
	}
	private static NativeImage downscaleByFactor(NativeImage input, int downscaleFactor) {
		int newWidth = input.getWidth() / downscaleFactor;
		int newHeight = input.getHeight() / downscaleFactor;

		NativeImage output = new NativeImage(newWidth, newHeight, false);

		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				long sumA = 0, sumR = 0, sumG = 0, sumB = 0;

				for (int dy = 0; dy < downscaleFactor; dy++) {
					for (int dx = 0; dx < downscaleFactor; dx++) {
						int px = Math.min(x * downscaleFactor + dx, input.getWidth() - 1);
						int py = Math.min(y * downscaleFactor + dy, input.getHeight() - 1);
						int pixel = input.getColorArgb(px, py);

						int a = (pixel >> 24) & 0xFF;
						int r = (pixel >> 16) & 0xFF;
						int g = (pixel >> 8) & 0xFF;
						int b = pixel & 0xFF;

						sumA += a;
						sumR += r;
						sumG += g;
						sumB += b;
					}
				}

				int area = downscaleFactor * downscaleFactor;
				int avgA = (int) (sumA / area);
				int avgR = (int) (sumR / area);
				int avgG = (int) (sumG / area);
				int avgB = (int) (sumB / area);

				int avgPixel = (avgA << 24) | (avgR << 16) | (avgG << 8) | avgB;
				output.setColorArgb(x, y, avgPixel);
			}
		}

		return output;
	}
}