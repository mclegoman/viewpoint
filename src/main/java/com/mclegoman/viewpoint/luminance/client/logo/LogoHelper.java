/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.logo;

import com.mclegoman.viewpoint.luminance.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.client.data.ClientData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3x2fStack;

public class LogoHelper {
	public static void renderDevelopmentOverlay(DrawContext context, int x, int y, int width, int height, boolean shouldRender, int xOffset, int yOffset) {
		if (shouldRender) context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of(Data.getVersion().getID(), "textures/gui/logo/development.png"), x + xOffset, y + yOffset, 0.0F, 0.0F, (int) (width * 0.75F), height / 4, (int) (width * 0.75F), height / 4);
	}
	public static void renderDevelopmentOverlay(DrawContext context, int x, int y, int width, int height, boolean shouldRender) {
		renderDevelopmentOverlay(context, x, y, width, height, shouldRender, 0, 0);
	}
	public static void createSplashText(DrawContext context, int width, int x, int y, TextRenderer textRenderer, Translation.Data splashText, float rotation) {
		if (splashText != null && !ClientData.minecraft.options.getHideSplashTexts().getValue()) {
			Matrix3x2fStack matrixStack = context.getMatrices();
			matrixStack.pushMatrix();
			matrixStack.translate(x + width, y);
			matrixStack.rotate((float) Math.toRadians(rotation));
			float scale = (1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * ((float)Math.PI * 2)) * 0.1F)) * 100.0F / (float)(textRenderer.getWidth(Translation.getText(splashText)) + 32);
			matrixStack.scale(scale, scale);
			context.drawCenteredTextWithShadow(textRenderer, Translation.getText(splashText), 0, -8, 0xFFFF00);
			matrixStack.popMatrix();
		}
	}
}