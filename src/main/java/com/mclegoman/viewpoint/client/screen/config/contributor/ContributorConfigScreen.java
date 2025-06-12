/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.contributor;

import com.mclegoman.viewpoint.client.config.ContributorConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;

public class ContributorConfigScreen extends AbstractConfigScreen {
	public ContributorConfigScreen(Screen parentScreen, int page) {
		super(parentScreen, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize contributor config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget contributorGrid = new GridWidget();
		contributorGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder contributorGridAdder = contributorGrid.createAdder(1);
		try {
			contributorGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "contributor.flip", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), ContributorConfig.config.flip.value(), Translation.Type.ONFF)}), (button) -> {
				ContributorConfig.toggleConfigValue(ContributorConfig.config.flip, false);
			}).width(300).build());
			contributorGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "contributor.flip.hold_perspective.back", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), ContributorConfig.config.flipOnHoldPerspectiveBack.value(), Translation.Type.ONFF)}), (button) -> {
				ContributorConfig.toggleConfigValue(ContributorConfig.config.flipOnHoldPerspectiveBack, false);
			}).width(300).build());
			contributorGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "contributor.flip.hold_perspective.front", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), ContributorConfig.config.flipOnHoldPerspectiveFront.value(), Translation.Type.ONFF)}), (button) -> {
				ContributorConfig.toggleConfigValue(ContributorConfig.config.flipOnHoldPerspectiveFront, false);
			}).width(300).build());
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/contributor/page1: " + error.getLocalizedMessage());
		}
		return contributorGrid;
	}
	public Screen getRefreshScreen() {
		return new ContributorConfigScreen(this.parentScreen, this.page);
	}
	public String getPageId() {
		return "contributor";
	}

	@Override
	protected void setParentScreen() {
		ContributorConfig.config.save();
		super.setParentScreen();
	}
}