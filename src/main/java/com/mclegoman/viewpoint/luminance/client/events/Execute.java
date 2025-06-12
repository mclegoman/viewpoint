/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.events;

import com.mclegoman.viewpoint.luminance.client.translation.Translation;
import com.mclegoman.viewpoint.luminance.common.data.Data;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.resource.ReloadableResourceManagerImpl;

public class Execute {
	public static void registerClientResourceReloaders(ReloadableResourceManagerImpl resourceManager) {
		Events.ClientResourceReloaders.registry.forEach((id, resourceReloader) -> resourceManager.registerReloader(resourceReloader));
	}
	public static void afterClientResourceReload() {
		Events.AfterClientResourceReload.registry.forEach((id, runnable) -> runnable.run());
	}
	public static void beforeInGameHudRender(DrawContext context, RenderTickCounter renderTickCounter) {
		Events.BeforeInGameHudRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(context, renderTickCounter);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterInGameHudRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void afterInGameHudRender(DrawContext context, RenderTickCounter renderTickCounter) {
		Events.AfterInGameHudRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(context, renderTickCounter);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterInGameHudRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void beforeGameRender() {
		Events.BeforeGameRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run();
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterGameRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void afterUiRender(ObjectAllocator allocator) {
		Events.AfterUiRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(ClientData.minecraft.getFramebuffer(), allocator);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterGameRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void afterUiBackgroundRender(ObjectAllocator allocator) {
		Events.AfterUiBackgroundRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(ClientData.minecraft.getFramebuffer(), allocator);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterScreenBackgroundRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void afterPanoramaRender(ObjectAllocator allocator) {
		Events.AfterPanoramaRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(ClientData.minecraft.getFramebuffer(), allocator);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterPanoramaRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void resize(int width, int height) {
		Events.OnResized.registry.forEach((id, runnable) -> runnable.run(width, height));
	}
	public static void beforeWorldRender() {
		Events.BeforeWorldRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run();
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute BeforeWorldRender event with id: {}: {}", id, error));
			}
		}));
	}
	public static void afterWorldRender(ObjectAllocator allocator) {
		Events.AfterWorldRender.registry.forEach(((id, runnable) -> {
			try {
				runnable.run(ClientData.minecraft.getFramebuffer(), allocator);
			} catch (Exception error) {
				Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to execute AfterWorldRender event with id: {}: {}", id, error));
			}
		}));
	}
}
