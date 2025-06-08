/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.events;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class Execute {
	public static Text getVariable(Identifier id, String[] args, RenderTickCounter tickCounter) {
		try {
			return Events.Variables.get(id).call(tickCounter, args);
		} catch (Exception error) {
			return Text.literal(id.toString() + Arrays.toString(args));
		}
	}
}
