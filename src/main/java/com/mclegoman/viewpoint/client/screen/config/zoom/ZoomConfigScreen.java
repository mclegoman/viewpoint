/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.zoom;

import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.hide.Hide;
import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ZoomConfigScreen extends AbstractConfigScreen {
	public ZoomConfigScreen(Screen parentScreen, int page) {
		super(parentScreen, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else if (this.page == 2) this.gridAdder.add(createPageTwo());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget zoomGrid = new GridWidget();
		zoomGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder zoomGridAdder = zoomGrid.createAdder(2);
		try {
			double zoomLevel = (double) PerspectiveConfig.config.zoomLevel.value() / 100;
			zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.level", new Object[]{Text.literal(PerspectiveConfig.config.zoomLevel.value() + "%")}, false), zoomLevel) {
				@Override
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.level", new Object[]{Text.literal(PerspectiveConfig.config.zoomLevel.value() + "%")}, false));
				}

				@Override
				protected void applyValue() {
					Zoom.setZoomLevel((float) ((value) * 100), false);
				}
			}, 1);
			double zoomIncrementSize = (double) (PerspectiveConfig.config.zoomIncrementSize.value() - 0.1) / 9.9;
			SliderWidget zoomIncrementSizeWidget = new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.increment_size", new Object[]{Text.literal(String.valueOf(PerspectiveConfig.config.zoomIncrementSize.value()))}, false), zoomIncrementSize) {
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.increment_size", new Object[]{Text.literal(String.valueOf(PerspectiveConfig.config.zoomIncrementSize.value()))}, false));
				}

				protected void applyValue() {
					Zoom.setIncrementSize((float) (((value) * 9.9) + 0.1), false);
				}
			};
			zoomIncrementSizeWidget.setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.increment_size", true)));
			zoomGridAdder.add(zoomIncrementSizeWidget, 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())}), (button) -> {
				Zoom.cycleZoomType(!hasShiftDown());
			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath(), true)}, true))).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.transition", new Object[]{Translation.getZoomTransitionTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomTransition.value())}), (button) -> {
				PerspectiveConfig.config.zoomTransition.setValue(Zoom.nextTransition(), false);
			}).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.scale_mode", new Object[]{Translation.getZoomScaleModeTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomScaleMode.value())}), (button) -> {
				PerspectiveConfig.config.zoomScaleMode.setValue(Zoom.nextScaleMode(), false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.scale_mode", new Object[]{Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.scale_mode." + PerspectiveConfig.config.zoomScaleMode.value(), true)}, true))).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.reset", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomReset.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.zoomReset, false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.reset", new Object[]{Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.reset." + PerspectiveConfig.config.zoomReset.value(), true)}, true))).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.cinematic", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomCinematic.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.zoomCinematic, false);

			}).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.enabled", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomEnabled.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.zoomEnabled, false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.enabled", new Object[]{Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.enabled." + PerspectiveConfig.config.zoomEnabled.value(), true)}, true))).build(), 1);
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/zoom/page1: " + error.getLocalizedMessage());
		}
		return zoomGrid;
	}
	private GridWidget createPageTwo() {
		GridWidget zoomGrid = new GridWidget();
		zoomGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder zoomGridAdder = zoomGrid.createAdder(2);
		try {
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.hide_hud", new Object[]{Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.hide_hud." + PerspectiveConfig.config.zoomHideHud.value())}), (button) -> {
				PerspectiveConfig.config.zoomHideHud.setValue(Hide.nextZoomHideHudMode(), false);

			}).tooltip(() -> Tooltip.of(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.hide_hud", new Object[]{Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.hide_hud." + PerspectiveConfig.config.zoomHideHud.value(), true)}, true))).build(), 1);
			zoomGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.show_percentage", new Object[]{Translation.getVariableTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomShowPercentage.value(), Translation.Type.ONFF)}), (button) -> {
				PerspectiveConfig.toggleConfigValue(PerspectiveConfig.config.zoomShowPercentage, false);

			}).build(), 1);
			zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.smooth_speed_in", new Object[]{Translation.getZoomSmoothSpeedTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomSmoothSpeedIn.value())}, false), (PerspectiveConfig.config.zoomSmoothSpeedIn.value() - 0.01F) / 1.99F) {
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.smooth_speed_in", new Object[]{Translation.getZoomSmoothSpeedTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomSmoothSpeedIn.value())}, false));
				}
				protected void applyValue() {
					PerspectiveConfig.config.zoomSmoothSpeedIn.setValue(Float.valueOf(String.format("%.2f", ((value) * 1.99F) + 0.01F)), false);
				}
			});
			zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.smooth_speed_out", new Object[]{Translation.getZoomSmoothSpeedTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomSmoothSpeedOut.value())}, false), (PerspectiveConfig.config.zoomSmoothSpeedOut.value() - 0.01F) / 1.99F) {
				protected void updateMessage() {
					setMessage(Translation.getConfigTranslation(Data.getVersion().getID(), "zoom.smooth_speed_out", new Object[]{Translation.getZoomSmoothSpeedTranslation(Data.getVersion().getID(), PerspectiveConfig.config.zoomSmoothSpeedOut.value())}, false));
				}
				protected void applyValue() {
					PerspectiveConfig.config.zoomSmoothSpeedOut.setValue(Float.valueOf(String.format("%.2f", ((value) * 1.99F) + 0.01F)), false);
				}
			});
			zoomGridAdder.add(new EmptyWidget(20, 20), 2);
			zoomGridAdder.add(new EmptyWidget(20, 20), 2);
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "Error creating config/zoom/page2: " + error.getLocalizedMessage());
		}
		return zoomGrid;
	}
	public Screen getRefreshScreen() {
		return new ZoomConfigScreen(this.parentScreen, this.page);
	}
	public String getPageId() {
		return "zoom";
	}
	public int getMaxPage() {
		return 2;
	}
}