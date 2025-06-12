/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Util;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class ScreenshotRecorder {
	public static void saveScreenshot(File directory, String fileName, Framebuffer framebuffer, int downscaleFactor) {
		takeScreenshot(framebuffer, downscaleFactor, (image) -> {
			directory.mkdir();
			File file = new File(directory, fileName);
			Util.getIoWorkerExecutor().execute(() -> {
				try {
					image.writeTo(file);
					image.close();
				} catch (Exception exception) {
					Data.getVersion().sendToLog(LogType.ERROR, "Couldn't save screenshot" + exception);
				}
			});
		});
	}
	public static void takeScreenshot(Framebuffer framebuffer, int downscaleFactor, Consumer<NativeImage> callback) {
		int width = framebuffer.textureWidth;
		int height = framebuffer.textureHeight;

		if (width % downscaleFactor != 0 || height % downscaleFactor != 0) {
			throw new IllegalArgumentException("Image size must be divisible by downscale factor");
		}

		int pixelCount = width * height;
		ByteBuffer buffer = BufferUtils.createByteBuffer(pixelCount * 4);

		framebuffer.beginRead();
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		framebuffer.endRead();

		NativeImage fullImage = new NativeImage(width, height, false);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int i = (x + y * width) * 4;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				int a = buffer.get(i + 3) & 0xFF;
				int color = (a << 24) | (r << 16) | (g << 8) | b;
				fullImage.setColor(x, y, color);
			}
		}

		int scaledWidth = width / downscaleFactor;
		int scaledHeight = height / downscaleFactor;
		NativeImage scaledImage = new NativeImage(scaledWidth, scaledHeight, false);

		for (int y = 0; y < scaledHeight; ++y) {
			for (int x = 0; x < scaledWidth; ++x) {
				int r = 0, g = 0, b = 0;

				for (int dy = 0; dy < downscaleFactor; ++dy) {
					for (int dx = 0; dx < downscaleFactor; ++dx) {
						int srcX = x * downscaleFactor + dx;
						int srcY = y * downscaleFactor + dy;
						int color = fullImage.getColor(srcX, srcY);

						r += (color >> 16) & 0xFF;
						g += (color >> 8) & 0xFF;
						b += color & 0xFF;
					}
				}

				int area = downscaleFactor * downscaleFactor;
				int avgColor = (0xFF << 24) | ((r / area) << 16) | ((g / area) << 8) | (b / area);
				scaledImage.setColor(x, scaledHeight - y - 1, avgColor);
			}
		}

		fullImage.close();
		callback.accept(scaledImage);
	}
}