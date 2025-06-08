/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.client.contributor.Contributor;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.luminance.LogType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ContributorConfigScreen extends AbstractConfigScreen {
	public ContributorConfigScreen(Screen parentScreen, boolean refresh, int page) {
		super(parentScreen, refresh, false, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize contributor config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget holdPerspectiveGrid = new GridWidget();
		holdPerspectiveGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder holdPerspectiveGridAdder = holdPerspectiveGrid.createAdder(2);
		holdPerspectiveGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "contributor.flip", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) Contributor.Config.selfFlipAlways, Translation.Type.ONFF)}), (button) -> {
			Contributor.Config.selfFlipAlways = !Contributor.Config.selfFlipAlways;
			this.refresh = true;
		}).build());
		holdPerspectiveGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "contributor.flip.hold_perspective.back", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) Contributor.Config.selfFlipHoldPerspectiveBack, Translation.Type.ONFF)}), (button) -> {
			Contributor.Config.selfFlipHoldPerspectiveBack = !Contributor.Config.selfFlipHoldPerspectiveBack;
			this.refresh = true;
		}).build());
		holdPerspectiveGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "contributor.flip.hold_perspective.front", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) Contributor.Config.selfFlipHoldPerspectiveFront, Translation.Type.ONFF)}), (button) -> {
			Contributor.Config.selfFlipHoldPerspectiveFront = !Contributor.Config.selfFlipHoldPerspectiveFront;
			this.refresh = true;
		}).build());
		holdPerspectiveGridAdder.add(new EmptyWidget(20, 20), 2);
		return holdPerspectiveGrid;
	}
	public Screen getRefreshScreen() {
		return new ContributorConfigScreen(this.parentScreen, false, this.page);
	}
	public String getPageId() {
		return "contributor";
	}
}