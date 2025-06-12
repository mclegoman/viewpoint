/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.panorama;

import net.minecraft.client.gl.Framebuffer;

import java.io.File;

public class ScreenshotRecorder {
	public static void saveScreenshot(File gameDirectory, String fileName, Framebuffer framebuffer) {
		net.minecraft.client.util.ScreenshotRecorder.saveScreenshot(gameDirectory, fileName, framebuffer, 4, (text) -> {});
	}
}