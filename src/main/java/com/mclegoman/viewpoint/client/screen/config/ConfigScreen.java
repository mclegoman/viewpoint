/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.screen.config.contributor.ContributorConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigLinkButtonWidget;
import com.mclegoman.viewpoint.client.ui.UIBackground;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.hide.HideConfigScreen;
import com.mclegoman.viewpoint.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.viewpoint.client.screen.config.overlays.OverlaysConfigScreen;
import com.mclegoman.viewpoint.client.screen.config.zoom.ZoomConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.GridWidget;

import java.net.URI;

public class ConfigScreen extends AbstractConfigScreen {
	public ConfigScreen(Screen parentScreen, int page) {
		super(parentScreen, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	protected void setParentScreen() {
		PerspectiveConfig.config.save();
		super.setParentScreen();
	}
	private GridWidget createPageOne() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		try {
			gridAdder.add(ConfigLinkButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "perspective"), (button) -> ClientData.minecraft.setScreen(new LinkScreen(this, URI.create("https://modrinth.com/mod/mclegoman-perspective"), true))).textures(ConfigLinkButtonWidget.getPerspectiveTextures()).width(Contributor.isClientContributor() ? 150 : 304).build(), Contributor.isClientContributor() ? 1 : 2);
			if (Contributor.isClientContributor()) {
				gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getCombinedText(Translation.getConfigTranslation(Data.getVersion().getID(), "contributor"), Translation.getTranslation(Data.getVersion().getID(), "more")), (button) -> ClientData.minecraft.setScreen(new ContributorConfigScreen(getRefreshScreen(), 1))).build());
			}
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getCombinedText(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom"), Translation.getTranslation(Data.getVersion().getID(), "more")), (button) -> ClientData.minecraft.setScreen(new ZoomConfigScreen(getRefreshScreen(), 1))).build());
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getCombinedText(Translation.getConfigTranslation(Data.getVersion().getID(), "hide"), Translation.getTranslation(Data.getVersion().getID(), "more")), (button) -> ClientData.minecraft.setScreen(new HideConfigScreen(getRefreshScreen(), 1))).build());
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getCombinedText(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective"), Translation.getTranslation(Data.getVersion().getID(), "more")), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(getRefreshScreen(), 1))).build());
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getCombinedText(Translation.getConfigTranslation(Data.getVersion().getID(), "overlays"), Translation.getTranslation(Data.getVersion().getID(), "more")), (button) -> ClientData.minecraft.setScreen(new OverlaysConfigScreen(getRefreshScreen(), 1))).build());
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "show_death_coordinates", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.showDeathCoordinates.value(), Translation.Type.ONFF)}), (button) -> PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.showDeathCoordinates)).build());
			gridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "ui_background", new Object[]{Translation.getUIBackgroundTranslation(Data.getVersion().getID(), UIBackground.getCurrentUIBackground().getId())}), (button) -> UIBackground.cycleUIBackgroundType(!hasShiftDown())).tooltip(() -> Tooltip.of(Translation.getUIBackgroundTranslation(Data.getVersion().getID(), UIBackground.getCurrentUIBackground().getId(), true))).build());
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/page1: " + error.getLocalizedMessage());
		}
		return grid;
	}
	public Screen getRefreshScreen() {
		return new ConfigScreen(this.parentScreen, this.page);
	}
	public String getPageId() {
		return "config";
	}
}