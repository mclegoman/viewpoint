/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config.zoom;

import com.mclegoman.viewpoint.client.screen.config.AbstractConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.zoom.Zoom;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.LogType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

public class ZoomConfigScreen extends AbstractConfigScreen {
	public ZoomConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
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
		GridWidget zoomGrid = new GridWidget();
		zoomGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder zoomGridAdder = zoomGrid.createAdder(2);
		double zoomLevel = (double) ((int) ConfigHelper.getConfig("zoom_level")) / 100;
		zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")}, false), zoomLevel) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.level", new Object[]{Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")}, false));
			}
			@Override
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_level", (int) ((value) * 100));
			}
		}, 1);
		double zoomIncrementSize = (double) ((int) ConfigHelper.getConfig("zoom_increment_size") - 1) / 9;
		SliderWidget zoomIncrementSizeWidget = new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig("zoom_increment_size")))}, false), zoomIncrementSize) {
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.increment_size", new Object[]{Text.literal(String.valueOf((int) ConfigHelper.getConfig("zoom_increment_size")))}, false));
			}
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_increment_size", (int) ((value) * 9) + 1);
			}
		};
		zoomIncrementSizeWidget.setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.increment_size", true)));
		zoomGridAdder.add(zoomIncrementSizeWidget, 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())}), (button) -> {
			Zoom.cycleZoomType(!hasShiftDown());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.type", new Object[]{Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath(), true)}, true))).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.transition", new Object[]{Translation.getZoomTransitionTranslation(Data.version.getID(), (String) ConfigHelper.getConfig("zoom_transition"))}), (button) -> {
			ConfigHelper.setConfig("zoom_transition", Zoom.nextTransition());
			this.refresh = true;
		}).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getZoomScaleModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig("zoom_scale_mode"))}), (button) -> {
			ConfigHelper.setConfig("zoom_scale_mode", Zoom.nextScaleMode());
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.scale_mode." + ConfigHelper.getConfig("zoom_scale_mode"), true)}, true))).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.reset", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("zoom_reset"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_reset", !(boolean) ConfigHelper.getConfig("zoom_reset"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.reset", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.reset." + ConfigHelper.getConfig("zoom_reset"), true)}, true))).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.cinematic", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("zoom_cinematic"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_cinematic", !(boolean) ConfigHelper.getConfig("zoom_cinematic"));
			this.refresh = true;
		}).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("zoom_enabled"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_enabled", !(boolean) ConfigHelper.getConfig("zoom_enabled"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "zoom.enabled." + ConfigHelper.getConfig("zoom_enabled"), true)}, true))).build(), 1);
		return zoomGrid;
	}
	private GridWidget createPageTwo() {
		GridWidget zoomGrid = new GridWidget();
		zoomGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder zoomGridAdder = zoomGrid.createAdder(2);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.hide_hud", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("zoom_hide_hud"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_hide_hud", !(boolean) ConfigHelper.getConfig("zoom_hide_hud"));
			this.refresh = true;
		}).build(), 1);
		zoomGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "zoom.show_percentage", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig("zoom_show_percentage"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig("zoom_show_percentage", !(boolean) ConfigHelper.getConfig("zoom_show_percentage"));
			this.refresh = true;
		}).build(), 1);
		zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.smooth_speed_in", new Object[]{Text.literal(String.valueOf((double) ConfigHelper.getConfig("zoom_smooth_speed_in")))}, false), ((double)ConfigHelper.getConfig("zoom_smooth_speed_in") - 0.001D) / 1.999D) {
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.smooth_speed_in", new Object[]{Text.literal(String.valueOf((double) ConfigHelper.getConfig("zoom_smooth_speed_in")))}, false));
			}
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_smooth_speed_in", Double.valueOf(String.format("%.2f", ((value) * 1.999) + 0.001D)));
			}
		});
		zoomGridAdder.add(new ConfigSliderWidget(zoomGridAdder.getGridWidget().getX(), zoomGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "zoom.smooth_speed_out", new Object[]{Text.literal(String.valueOf((double) ConfigHelper.getConfig("zoom_smooth_speed_out")))}, false), ((double)ConfigHelper.getConfig("zoom_smooth_speed_out") - 0.001D) / 1.999D) {
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(),  "zoom.smooth_speed_out", new Object[]{Text.literal(String.valueOf((double) ConfigHelper.getConfig("zoom_smooth_speed_out")))}, false));
			}
			protected void applyValue() {
				ConfigHelper.setConfig("zoom_smooth_speed_out", Double.valueOf(String.format("%.2f", ((value) * 1.999) + 0.001D)));
			}
		});
		zoomGridAdder.add(new EmptyWidget(20, 20), 2);
		zoomGridAdder.add(new EmptyWidget(20, 20), 2);
		return zoomGrid;
	}
	public Screen getRefreshScreen() {
		return new ZoomConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "zoom";
	}
	public int getMaxPage() {
		return 2;
	}
}