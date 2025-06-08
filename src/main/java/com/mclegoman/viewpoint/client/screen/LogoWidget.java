package com.mclegoman.viewpoint.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LogoWidget extends ClickableWidget {
	private static Identifier texture;
	public LogoWidget(int x, int y, Identifier texture) {
		super(x, y, 256, 64, Text.empty());
		LogoWidget.texture = texture;
	}
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		renderLogo(context, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}
	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height) {
		context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, 0.0F, 0.0F, width, (int) (height * 0.6875), width, height);
	}
}