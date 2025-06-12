/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.widget;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.common.util.Identifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.Callable;

public class ConfigLinkButtonWidget extends ConfigButtonWidget {
	public static ButtonTextures LINK_TEXTURES;
	protected ConfigLinkButtonWidget(int x, int y, int width, int height, Callable<Text> message, PressAction onPress, NarrationSupplier narrationSupplier, Callable<Tooltip> tooltip, ButtonTextures textures) throws Exception {
		super(x, y, width, height, message, onPress, narrationSupplier, tooltip);
		LINK_TEXTURES = textures;
	}
	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		context.drawGuiTexture(LINK_TEXTURES.get(this.active, this.isSelected()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		int i = this.active ? 16777215 : 10526880;
		this.drawMessage(context, ClientData.minecraft.textRenderer, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}
	public static Builder builder(Callable<Text> message, PressAction onPress) {
		return new Builder(message, onPress);
	}
	@Environment(EnvType.CLIENT)
	public static class Builder extends ConfigButtonWidget.Builder {
		public ButtonTextures textures;
		public Builder(Callable<Text> message, PressAction onPress) {
			super(message, onPress);
			textures = getTextures();
		}
		public Builder textures(ButtonTextures textures) {
			this.textures = textures;
			return this;
		}
		@Override
		public ConfigLinkButtonWidget build() throws Exception {
			return new ConfigLinkButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier, this.tooltip, this.textures);
		}
	}
	public static ButtonTextures getTextures() {
		return new ButtonTextures(Identifiers.LINK_BUTTON, Identifiers.LINK_BUTTON_DISABLED, Identifiers.LINK_BUTTON_HIGHLIGHTED);
	}
	public static ButtonTextures getPerspectiveTextures() {
		return new ButtonTextures(Identifiers.LINK_BUTTON_PERSPECTIVE, Identifiers.LINK_BUTTON_DISABLED_PERSPECTIVE, Identifiers.LINK_BUTTON_HIGHLIGHTED_PERSPECTIVE);
	}
}
