/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.events.runnables;

import net.minecraft.client.render.RenderTickCounter;

public class Runnables {
	public interface Variable<T> {
		T call(RenderTickCounter tickCounter, String... args);
	}
}
