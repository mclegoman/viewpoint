/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.ui;

import com.mclegoman.viewpoint.luminance.common.util.IdentifierHelper;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.config.value.ConfigIdentifier;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.common.util.Identifiers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class UIBackground {
	private static final List<UIBackgroundData> uiBackgroundTypes = new ArrayList<>();
	public static void init() {
		registerUIBackground(new UIBackgroundData.Builder(Identifiers.DEFAULT).build());
		registerUIBackground(new UIBackgroundData.Builder(Identifiers.GAUSSIAN).shaderId(Identifier.of(Data.getVersion().getID(), "gaussian")).build());
		registerUIBackground(new UIBackgroundData.Builder(Identifiers.LEGACY).renderWorld(context -> {
				context.fillGradient(0, 0, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), -1072689136, -804253680);
			}).renderMenu(context -> {
				context.drawTexture(RenderLayer::getGuiTextured, getUiBackgroundTextureFromConfig(), 0, 0, 0, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
				context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(Data.getVersion().getID(), "textures/gui/uibackground_menu_background.png"), 0, 0, 0, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
		}).renderPanorama(false).renderShader(false).build());
		registerUIBackground(new UIBackgroundData.Builder(Identifiers.CLASSIC).renderWorld(context -> {
				context.fillGradient(0, 0, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), -1072689136, -804253680);
			}).renderMenu(context -> {
				context.drawTexture(RenderLayer::getGuiTextured, getUiBackgroundTextureFromConfig(), 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
				context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(Data.getVersion().getID(), "textures/gui/uibackground_menu_background.png"), 0, 0, 0, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
			}).renderTitleScreen(context -> {
				context.drawTexture(RenderLayer::getGuiTextured, getUiBackgroundTextureFromConfig(), 0, 0, 0.0F, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
				context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(Data.getVersion().getID(), "textures/gui/uibackground_menu_background.png"), 0, 0, 0, 0.0F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 32, 32);
		}).renderPanorama(false).renderTitleScreenPanorama(false).renderShader(false).build());
		registerUIBackground(new UIBackgroundData.Builder(Identifiers.NONE).renderShader(false).renderDarkening(false).build());
	}
	public static void registerUIBackground(UIBackgroundData data) {
		if (!ClientData.minecraft.isFinishedLoading()) {
			boolean alreadyRegistered = isValidUIBackground(data.getId());
			if (!alreadyRegistered) uiBackgroundTypes.add(data);
			else Data.getVersion().sendToLog(LogType.WARN, Translation.getString("UI Background with id '{}' could not be registered: UI Background is already registered!", data.getId()));
		} else Data.getVersion().sendToLog(LogType.WARN, Translation.getString("UI Background with id '{}' could not be registered: Config has already been initialized!", data.getId()));
	}
	public static boolean isValidUIBackground(Identifier id) {
		for (UIBackgroundData uiData : uiBackgroundTypes) {
			if (id.equals(uiData.getId())) return true;
		}
		return false;
	}
	public static void cycleUIBackgroundType() {
		cycleUIBackgroundType(true);
	}
	public static void cycleUIBackgroundType(boolean direction) {
		int currentIndex = uiBackgroundTypes.indexOf(getCurrentUIBackground());
		PerspectiveConfig.config.uiBackground.setValue(ConfigIdentifier.of(uiBackgroundTypes.get(direction ? (currentIndex + 1) % uiBackgroundTypes.size() : (currentIndex - 1 + uiBackgroundTypes.size()) % uiBackgroundTypes.size()).getId()), false);
	}
	public static UIBackgroundData getCurrentUIBackground() {
		return getUIBackgroundType(PerspectiveConfig.config.uiBackground.value().getIdentifier());
	}
	public static UIBackgroundData getUIBackgroundType(Identifier type) {
		for (UIBackgroundData data : uiBackgroundTypes) {
			if (data.getId().equals(type)) return data;
		}
		return UIBackgroundData.Builder.getFallback();
	}
	public static boolean isRegisteredUIBackgroundType(Identifier type) {
		for (UIBackgroundData data : uiBackgroundTypes) {
			if (data.getId().equals(type)) return true;
		}
		return false;
	}
	public static Identifier getUiBackgroundTextureFromConfig() {
		Identifier uiBackgroundTexture = PerspectiveConfig.config.uiBackgroundTexture.value().getIdentifier();
		String namespace = IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, uiBackgroundTexture.getNamespace());
		String key = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, uiBackgroundTexture.getPath());
		return (namespace != null && key != null) ? Identifier.of(namespace, (!key.startsWith("textures/") ? "textures/" : "") + key + (!key.endsWith(".png") ? ".png" : "")) : Identifier.of("minecraft", "textures/block/dirt.png");
	}
	public static Identifier getUIBackgroundId() {
		return Identifiers.UI_BACKGROUND;
	}
	public interface Runnable {
		void run(DrawContext context);
	}
}