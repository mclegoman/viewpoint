/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.hold_perspective.HoldPerspectiveConfigScreen;
import com.mclegoman.viewpoint.client.screen.config.zoom.ZoomConfigScreen;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;

public class ConfigScreen extends AbstractConfigScreen {
	public ConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget grid = new GridWidget();
		grid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder gridAdder = grid.createAdder(2);
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom"), (button) -> ClientData.minecraft.setScreen(new ZoomConfigScreen(getRefreshScreen(), false, false, 1))).build());
		gridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective"), (button) -> ClientData.minecraft.setScreen(new HoldPerspectiveConfigScreen(getRefreshScreen(), false, false, 1))).build());
		gridAdder.add(new EmptyWidget(20, 20), 2);
		gridAdder.add(new EmptyWidget(20, 20), 2);
		gridAdder.add(new EmptyWidget(20, 20), 2);
		return grid;
	}
	public Screen getRefreshScreen() {
		return new ConfigScreen(this.parentScreen, false, this.saveOnClose, this.page);
	}
	public String getPageId() {
		return "config";
	}
	public boolean saveOnClose() {
		return true;
	}
}