/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.hold_perspective;

import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class HoldPerspectiveConfigScreen extends AbstractConfigScreen {
	public HoldPerspectiveConfigScreen(Screen parentScreen, int page) {
		super(parentScreen, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize hold_perspective config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget holdPerspectiveGrid = new GridWidget();
		holdPerspectiveGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder holdPerspectiveGridAdder = holdPerspectiveGrid.createAdder(2);
		try {
			holdPerspectiveGridAdder.add(new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.back.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveBackMultiplier.value())}, false), ((PerspectiveConfig.config.holdPerspectiveBackMultiplier.value() - 0.5F) / 15.5F)) {
				@Override
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.back.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveBackMultiplier.value())}, false));
				}
				@Override
				protected void applyValue() {
					PerspectiveConfig.config.holdPerspectiveBackMultiplier.setValue(Float.valueOf(String.format("%.2f", ((value * 15.5F) + 0.5F))), false);
				}
			}).setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.back.multiplier", true)));
			holdPerspectiveGridAdder.add(new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.front.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveFrontMultiplier.value())}, false), ((PerspectiveConfig.config.holdPerspectiveFrontMultiplier.value() - 0.5F) / 15.5F)) {
				@Override
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.front.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveFrontMultiplier.value())}, false));
				}
				@Override
				protected void applyValue() {
					PerspectiveConfig.config.holdPerspectiveFrontMultiplier.setValue(Float.valueOf(String.format("%.2f", ((value * 15.5F) + 0.5F))), false);
				}
			}).setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.front.multiplier", true)));


			holdPerspectiveGridAdder.add(new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.top.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveTopMultiplier.value())}, false), ((PerspectiveConfig.config.holdPerspectiveTopMultiplier.value() - 0.5F) / 15.5F)) {
				@Override
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.top.multiplier", new Object[]{String.format("%.2f", PerspectiveConfig.config.holdPerspectiveTopMultiplier.value())}, false));
				}
				@Override
				protected void applyValue() {
					PerspectiveConfig.config.holdPerspectiveTopMultiplier.setValue(Float.valueOf(String.format("%.2f", ((value * 15.5F) + 0.5F))), false);
				}
			}).setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.top.multiplier", true)));



			double holdPerspectiveMultiplierIncrementSize = (double) (PerspectiveConfig.config.holdPerspectiveMultiplierIncrementSize.value() - 1) / 9;
			SliderWidget holdPerspectiveMultiplierIncrementSizeWidget = new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.increment_size", new Object[]{Text.literal(String.valueOf(PerspectiveConfig.config.holdPerspectiveMultiplierIncrementSize.value()))}, false), holdPerspectiveMultiplierIncrementSize) {
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.increment_size", new Object[]{Text.literal(String.valueOf(PerspectiveConfig.config.holdPerspectiveMultiplierIncrementSize.value()))}, false));
				}
				protected void applyValue() {
					PerspectiveConfig.config.holdPerspectiveMultiplierIncrementSize.setValue((int) ((value) * 9) + 1, false);
				}
			};
			holdPerspectiveMultiplierIncrementSizeWidget.setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.increment_size", true)));
			holdPerspectiveGridAdder.add(holdPerspectiveMultiplierIncrementSizeWidget);
			holdPerspectiveGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.back.hide_hud", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.holdPerspectiveBackHideHud.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.holdPerspectiveBackHideHud, false);
			}).build());
			holdPerspectiveGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.front.hide_hud", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.holdPerspectiveFrontHideHud.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.holdPerspectiveFrontHideHud, false);
			}).build());
			holdPerspectiveGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "hold_perspective.perspective", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.perspectiveMultiplier.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.perspectiveMultiplier, false);
			}).width(300).build(), 2);
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/hold_perspective/page1: " + error.getLocalizedMessage());
		}
		return holdPerspectiveGrid;
	}
	public Screen getRefreshScreen() {
		return new HoldPerspectiveConfigScreen(this.parentScreen, this.page);
	}
	public String getPageId() {
		return "hold_perspective";
	}
}