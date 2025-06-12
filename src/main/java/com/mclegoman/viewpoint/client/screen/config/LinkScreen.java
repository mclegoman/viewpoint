/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.client.screen.widget.ConfigLinkButtonWidget;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.util.Util;

import java.net.URI;

public class LinkScreen extends AbstractConfigScreen {
	private final URI uri;
	private final boolean trusted;
	public LinkScreen(Screen parentScreen, URI uri, boolean trusted) {
		super(parentScreen, 1);
		this.uri = uri;
		this.trusted = trusted;
	}
	public void init() {
		try {
			this.grid.getMainPositioner().alignHorizontalCenter().margin(2);
			this.gridAdder = grid.createAdder(3);
			this.gridAdder.add(new EmptyWidget(20, 20), 3);
			this.gridAdder.add(new MultilineTextWidget(Translation.getText(this.trusted ? "chat.link.confirmTrusted" : "chat.link.confirm", true), ClientData.minecraft.textRenderer).setCentered(true), 3);
			this.gridAdder.add(new MultilineTextWidget(Translation.getText(this.uri.toString(), false), ClientData.minecraft.textRenderer).setCentered(true), 3);
			this.gridAdder.add(new EmptyWidget(20, 20), 3);
			this.gridAdder.add(ConfigLinkButtonWidget.builder(() -> Translation.getText("chat.link.open", true), (button) -> {
				Util.getOperatingSystem().open(this.uri);
				ClientData.minecraft.setScreen(this.parentScreen);
			}).width(100).build());
			this.gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getText("chat.copy", true), (button) -> {
				ClientData.minecraft.keyboard.setClipboard(this.uri.toString());
				ClientData.minecraft.setScreen(this.parentScreen);
			}).width(100).build());
			this.gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getText("gui.cancel", true), (button) -> ClientData.minecraft.setScreen(this.parentScreen)).width(100).build());
			if (this.page != 1) shouldClose = true;
			this.grid.refreshPositions();
			this.grid.forEachChild(this::addDrawableChild);
			initTabNavigation();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize link screen: {}", getPageTitle(), error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
}
