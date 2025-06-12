/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.logo;


import com.mclegoman.viewpoint.luminance.client.logo.LogoHelper;
import com.mclegoman.viewpoint.luminance.client.translation.Translation;
import com.mclegoman.viewpoint.luminance.common.util.DateHelper;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.events.PerspectiveEvents;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.common.util.Identifiers;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class PerspectiveLogo {
	public static void init() {
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.PRIDE, new PrideLogoDataLoader());
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.SPLASHES, new SplashesDataloader());
	}
	public static LogoData getDefaultLogo(String arg) {
		return new LogoData("default", Data.getVersion().getID(), Identifier.of(Data.getVersion().getID(), getLogoTexture("default", Data.getVersion().getID())), Identifier.of(Data.getVersion().getID(), getIconTexture("default", Data.getVersion().getID())));
	}
	public static LogoData getExperimentalLogo(String arg) {
		return new LogoData("experimental", Data.getVersion().getID(), Identifier.of(Data.getVersion().getID(), getLogoTexture("experimental", Data.getVersion().getID())), Identifier.of(Data.getVersion().getID(), getIconTexture("experimental", Data.getVersion().getID())));
	}
	public static String getLogoTexture(String type, String id) {
		return "textures/logos/" + type + "/" + id + ".png";
	}
	public static String getIconTexture(String type, String id) {
		return "textures/icons/" + type + "/" + id + ".png";
	}
	public static boolean isPride() {
		return isActuallyPride() || isForcePride();
	}
	public static boolean isActuallyPride() {
		return DateHelper.isPride();
	}
	public static boolean isForcePride() {
		return PerspectiveConfig.config.forcePride.value();
	}
	private static LogoData getPrideLogo(String arg) {
		if (!PerspectiveConfig.config.forcePrideType.value().equals("random")) return getPrideLogoFromId(PerspectiveConfig.config.forcePrideType.value(), arg);
		else return PrideLogoDataLoader.getLogo();
	}
	public static LogoData getPrideLogoFromId(String id, String arg) {
		for (LogoData logoData : PrideLogoDataLoader.registry) if (id.equals(logoData.getId())) return logoData;
		return getDefaultLogo(arg);
	}
	public static Logo getLogo(Logo.Type type) {
		return getLogo(type, "");
	}
	public static Logo getLogo(Logo.Type type, String arg) {
        return new Logo(type == Logo.Type.PRIDE ? getPrideLogo(arg) : getDefaultLogo(arg));
    }
	public static Identifier getLogoTexture() {
		return getLogo(isPride() ? Logo.Type.PRIDE : Logo.Type.DEFAULT).getLogoTexture();
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height, Identifier logoTexture) {
		renderLogo(context, x, y, width, height, logoTexture, false);
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height, Identifier logoTexture, boolean flip) {
		MatrixStack matrixStack = context.getMatrices();
		matrixStack.push();
		if (flip) {
			matrixStack.translate(0, -(110 * (height / 256.0F)), 0);
			matrixStack.translate(x + width / 2.0, y + height / 2.0F, 0);
			matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180.0F));
			matrixStack.translate(-(x + width / 2.0), -(y + height / 2.0F), 0);
		}
		context.drawTexture(RenderLayer::getGuiTextured, logoTexture, x, y, 0.0F, 0.0F, width, (int) (height * 0.6875F), width, height);
		LogoHelper.renderDevelopmentOverlay(context, (int) ((x + ((float) width / 2)) - ((width * 0.75F) / 2)), (int) (y + (height - (height * 0.54F))), width, height, Data.getVersion().isDevelopmentBuild(), 0, 0);
		matrixStack.pop();
	}
	public record Logo(LogoData data) {
		public Identifier getIconTexture() {
			return data.getIconTexture();
		}
		public Identifier getLogoTexture() {
			return data.getLogoTexture();
		}
		public enum Type implements StringIdentifiable {
			DEFAULT("default"),
			PRIDE("pride");
			private final String name;
			Type(String name) {
				this.name = name;
			}
			public String toString() {
					return this.name;
				}
			public String asString() {
				return this.name;
			}
		}
	}
	public static class Widget extends ClickableWidget {
		public Widget(int x, int y) {
			super(x, y, 256, 64, Text.empty());
		}
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			renderLogo(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), getLogoTexture());
			createSplashText(context, this.getWidth(), this.getX(), this.getY() + 32, ClientData.minecraft.textRenderer, SplashesDataloader.getSplashText(), -20.0F, false);
		}
		@Override
		protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		}
		@Override
		protected boolean isValidClickButton(int button) {
			return false;
		}
	}
	public static void createSplashText(DrawContext context, int width, int x, int y, TextRenderer textRenderer, Translation.Data splashText, float rotation, boolean flip) {
		if (splashText != null && !(Boolean) ClientData.minecraft.options.getHideSplashTexts().getValue()) {
			MatrixStack matrixStack = context.getMatrices();
			matrixStack.push();
			matrixStack.translate((float)(x + width), (float)y, 0.0F);
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation + (flip ? 180.0F : 0.0F)));
			float scale = (1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F)) * 100.0F / (float)(textRenderer.getWidth(Translation.getText(splashText)) + 32);
			matrixStack.scale(scale, scale, scale);
			context.drawCenteredTextWithShadow(textRenderer, Translation.getText(splashText), 0, -8 + (flip ? textRenderer.fontHeight : 0), 16776960);
			matrixStack.pop();
		}
	}
}
