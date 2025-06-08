/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.overlays;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.hud.Overlays;
import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.config.value.QualityToggle;
import com.mclegoman.viewpoint.luminance.LogType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;

public class OverlaysConfigScreen extends AbstractConfigScreen {
	public OverlaysConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else if (this.page == 2) this.gridAdder.add(createPageTwo());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget overlaysGrid = new GridWidget();
		overlaysGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder overlaysGridAdder = overlaysGrid.createAdder(2);
		try {
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.version_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("version_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("version_overlay", !(boolean)ConfigHelper.getConfig("version_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.position_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("position_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("position_overlay", !(boolean)ConfigHelper.getConfig("position_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.time_overlay", new Object[]{Translation.getTimeOverlayTranslation(Data.version.getID(), Overlays.getCurrentTimeOverlay())}), (button) -> {
				Overlays.cycleTimeOverlay(!hasShiftDown());
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.day_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("day_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("day_overlay", !(boolean)ConfigHelper.getConfig("day_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.biome_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("biome_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("biome_overlay", !(boolean)ConfigHelper.getConfig("biome_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.looking_at_overlay", new Object[]{Translation.getQualityTranslation(Data.version.getID(), QualityToggle.valueOf((String) ConfigHelper.getConfig("looking_at_overlay")))}), (button) -> {
				ConfigHelper.setConfig("looking_at_overlay", (switch (QualityToggle.valueOf((String) ConfigHelper.getConfig("looking_at_overlay"))) {
					case off -> hasShiftDown() ? QualityToggle.fancy : QualityToggle.fast;
					case fast -> hasShiftDown() ? QualityToggle.off : QualityToggle.fancy;
					case fancy -> hasShiftDown() ? QualityToggle.fast : QualityToggle.off;
				}));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.deaths_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean)ConfigHelper.getConfig("deaths_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("deaths_overlay", !(boolean)ConfigHelper.getConfig("deaths_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.totems_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean)ConfigHelper.getConfig("totems_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("totems_overlay", !(boolean)ConfigHelper.getConfig("totems_overlay"));
			}).build());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, "error occurred on overlays screen: " + error.getLocalizedMessage());
		}
		return overlaysGrid;
	}
	private GridWidget createPageTwo() {
		GridWidget overlaysGrid = new GridWidget();
		overlaysGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder overlaysGridAdder = overlaysGrid.createAdder(2);
		try {
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.cps_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean)ConfigHelper.getConfig("cps_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("cps_overlay", !(boolean)ConfigHelper.getConfig("cps_overlay"));
			}).build());
			overlaysGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.version.getID(), "overlays.armor_overlay", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean)ConfigHelper.getConfig("armor_overlay"), Translation.Type.ONFF)}), (button) -> {
				ConfigHelper.setConfig("armor_overlay", !(boolean)ConfigHelper.getConfig("armor_overlay"));
			}).build());
			overlaysGridAdder.add(new EmptyWidget(20, 20), 2);
			overlaysGridAdder.add(new EmptyWidget(20, 20), 2);
			overlaysGridAdder.add(new EmptyWidget(20, 20), 2);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, "error occurred on overlays screen: " + error.getLocalizedMessage());
		}
		return overlaysGrid;
	}
	public Screen getRefreshScreen() {
		return new OverlaysConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "overlays";
	}
	public int getMaxPage() {
		return 2;
	}
}