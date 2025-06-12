/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.hide;

import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class HideConfigScreen extends AbstractConfigScreen {
	public HideConfigScreen(Screen parentScreen, int page) {
		super(parentScreen, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget hideGrid = new GridWidget();
		hideGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder hideGridAdder = hideGrid.createAdder(2);
		try {
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.block_outline", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), !PerspectiveConfig.config.hideBlockOutline.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideBlockOutline, false);

			}).build());
			double blockOutlineLevel = (double) PerspectiveConfig.config.blockOutline.value() / 100;
			hideGridAdder.add(new ConfigSliderWidget(hideGridAdder.getGridWidget().getX(), hideGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "hide.block_outline", new Object[]{Text.literal(PerspectiveConfig.config.blockOutline.value() + "%")}, false), blockOutlineLevel) {
				@Override
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(),  "hide.block_outline.level", new Object[]{Text.literal(PerspectiveConfig.config.blockOutline.value() + "%")}, false));
				}
				@Override
				protected void applyValue() {
					PerspectiveConfig.config.blockOutline.setValue((int) ((value) * 100), false);
				}
			});
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.rainbow_block_outline", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.rainbowBlockOutline.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.rainbowBlockOutline, false);

			}).build());
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.crosshair", new Object[]{Translation.getCrosshairTranslation(Data.getVersion().getID(), PerspectiveConfig.config.crosshairType.value())}), (button) -> {
				PerspectiveConfig.config.crosshairType.setValue(Hide.nextCrosshairMode(), false);

			}).build());
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_armor", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hideArmor.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideArmor, false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_armor", true))).build());
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_nametags", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hideNametags.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideNametags, false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_nametags", true))).build());
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_players", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hidePlayers.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hidePlayers, false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hide.hide_players", true))).build());
			hideGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hide.show_message", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.hideShowMessage.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.hideShowMessage, false);

			}).build());
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/hide/page1: " + error.getLocalizedMessage());
		}
		return hideGrid;
	}
	public Screen getRefreshScreen() {
		return new HideConfigScreen(this.parentScreen, this.page);
	}
	public String getPageId() {
		return "hide";
	}
}