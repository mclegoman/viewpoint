/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mojang.blaze3d.buffers.BufferType;
import com.mojang.blaze3d.buffers.BufferUsage;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Util;

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
		int i = framebuffer.textureWidth;
		int j = framebuffer.textureHeight;
		GpuTexture gpuTexture = framebuffer.getColorAttachment();

		if (gpuTexture == null) {
			throw new IllegalStateException("Tried to capture screenshot of an incomplete framebuffer");
		} else if (i % downscaleFactor != 0 || j % downscaleFactor != 0) {
			throw new IllegalArgumentException("Image size is not divisible by downscale factor");
		}

		int pixelSize = gpuTexture.getFormat().pixelSize();
		GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Screenshot buffer", BufferType.PIXEL_PACK, BufferUsage.STATIC_READ, i * j * pixelSize);
		CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

		commandEncoder.copyTextureToBuffer(gpuTexture, gpuBuffer, 0, () -> {
			try (GpuBuffer.ReadView readView = commandEncoder.readBuffer(gpuBuffer)) {
				ByteBuffer data = readView.data();
				int width = i / downscaleFactor;
				int height = j / downscaleFactor;

				NativeImage nativeImage = new NativeImage(width, height, false);

				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						int r = 0, g = 0, b = 0;

						for (int dy = 0; dy < downscaleFactor; ++dy) {
							for (int dx = 0; dx < downscaleFactor; ++dx) {
								int srcX = x * downscaleFactor + dx;
								int srcY = y * downscaleFactor + dy;
								int index = (srcX + srcY * i) * pixelSize;
								int color = data.getInt(index);

								r += (color >> 16) & 0xFF;
								g += (color >> 8) & 0xFF;
								b += color & 0xFF;
							}
						}

						int area = downscaleFactor * downscaleFactor;
						int avgColor = (0xFF << 24) | ((r / area) << 16) | ((g / area) << 8) | (b / area);
						nativeImage.setColor(x, height - y - 1, avgColor);
					}
				}

				callback.accept(nativeImage);
			}

			gpuBuffer.close();
		}, 0);
	}

}