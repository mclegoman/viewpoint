/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.util;

import com.mclegoman.viewpoint.luminance.client.events.Events;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.client.data.ClientData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MessageOverlay {
	public static Text message;
	public static float remaining;
	public static void init() {
		Events.AfterInGameHudRender.register(Identifier.of(Data.getVersion().getID(), "message_overlay"), (context, renderTickCounter) -> {
			if (remaining > 0 && message != null && !message.equals(Text.empty())) context.drawCenteredTextWithShadow(ClientData.minecraft.textRenderer, message, (int) (ClientData.minecraft.getWindow().getScaledWidth() / 2.0F), 23, 16777215 | (((int) Math.min(((remaining + 1) - ClientData.minecraft.getRenderTickCounter().getTickProgress(true)) * 255.0F / 20.0F, 255.0F)) << 24 & -16777216));
        });
	}
	public static void tick() {
		if (remaining > 0) remaining--;
		else clearOverlay();
	}
	public static void setOverlay(Text text) {
		message = text;
		remaining = 40;
	}
	public static void clearOverlay() {
		remaining = 0;
		message = Text.empty();
	}
}