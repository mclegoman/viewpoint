/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.events;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.ObjectAllocator;

public class Runnables {
	public interface InGameHudRender {
		void run(DrawContext context, RenderTickCounter renderTickCounter);
	}
	public interface OnResized {
		void run(int width, int height);
	}
	public interface GameRender {
		void run(Framebuffer framebuffer, ObjectAllocator objectAllocator);
	}
}