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
import net.minecraft.util.Util;

import java.io.File;

public class ScreenshotRecorder {
	public static void saveScreenshot(File directory, String fileName, Framebuffer framebuffer, int downscaleFactor) {
		net.minecraft.client.util.ScreenshotRecorder.takeScreenshot(framebuffer, downscaleFactor, (image) -> {
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
}