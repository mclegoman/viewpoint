/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.logo.PerspectiveLogo;
import com.mclegoman.viewpoint.client.screen.widget.ConfigButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractConfigScreen extends Screen {
	protected int page;
	protected final Screen parentScreen;
	protected final GridWidget grid;
	protected GridWidget.Adder gridAdder;
	protected boolean shouldClose;
	public AbstractConfigScreen(Screen parentScreen, int page) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = parentScreen;
		this.page = page;
	}
	public void init() {
		super.init();
		this.grid.getMainPositioner().alignHorizontalCenter().margin(getGridMargin());
		this.gridAdder = grid.createAdder(1);
		this.gridAdder.add(new EmptyWidget(20, 20), 1);
		this.gridAdder.add(new EmptyWidget(20, 20), 1);
		this.gridAdder.add(new EmptyWidget(20, 20), 1);
	}
	public void postInit() {
		this.gridAdder.add(createFooter());
		this.grid.refreshPositions();
		this.grid.forEachChild(this::addDrawableChild);
		initTabNavigation();
	}
	public void tick() {
		try {
			if (this.isDefaults) {
				this.defaultsTicksRemaining--;
				if (this.defaultsTicksRemaining <= 0) {
					this.isDefaults = false;
					this.defaultsTicksRemaining = 0;
				}
			}
			if (this.shouldClose) {
				setParentScreen();
			}
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, Translation.getString("Failed to tick perspective$config screen: {}", error));
		}
	}
	protected void setParentScreen() {
		ClientData.minecraft.setScreen(this.parentScreen);
	}
	protected GridWidget createFooter() {
		GridWidget footerGrid = new GridWidget();
		footerGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder footerGridAdder = footerGrid.createAdder(3);
		try {
			footerGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "reset"), (button) -> {
				PerspectiveConfig.config.reset(false);
				ClientData.minecraft.setScreen(getRefreshScreen());
			}).build());
			footerGridAdder.add(ConfigButtonWidget.builder(() -> Translation.getConfigTranslation(Data.getVersion().getID(), "back"), (button) -> {
				if (this.page <= 1) {
					this.shouldClose = true;
				} else {
					this.page -= 1;
					ClientData.minecraft.setScreen(getRefreshScreen());
				}
			}).width(73).build());
			ButtonWidget nextButtonWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.getVersion().getID(), "next"), (button) -> {
				if (!(this.page >= getMaxPage())) {
					this.page += 1;
					ClientData.minecraft.setScreen(getRefreshScreen());
				}
			}).width(73).build();
			if (this.page >= getMaxPage()) nextButtonWidget.active = false;
			footerGridAdder.add(nextButtonWidget);
		} catch (Exception error) {
			Data.getVersion().sendToLog(LogType.ERROR, "An error occurred whilst creating config footer: " + error.getLocalizedMessage());
		}
		return footerGrid;
	}
	public void initTabNavigation() {
		SimplePositioningWidget.setPos(this.grid, getNavigationFocus());
	}
	public Text getNarratedTitle() {
		return ScreenTexts.joinSentences();
	}
	public boolean shouldCloseOnEsc() {
		return false;
	}
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getCode()) {
			if (page <= 1) {
				this.shouldClose = true;
			} else {
				this.page -= 1;
				ClientData.minecraft.setScreen(getRefreshScreen());
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (hasControlDown() && keyCode == GLFW.GLFW_KEY_F5) PerspectiveConfig.init();
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	private boolean isDefaults;
	private int defaultsTicksRemaining;
	private String defaultsType;
	private void setDefaults(String type) {
		this.isDefaults = true;
		this.defaultsTicksRemaining = 20;
		this.defaultsType = type;
	}
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (this.isDefaults) context.drawTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.getVersion().getID(), this.defaultsType), this.width - textRenderer.getWidth(Translation.getConfigTranslation(Data.getVersion().getID(), this.defaultsType)) - 2, 2, 0xFFFFFF);
		context.drawTextWithShadow(textRenderer, Translation.getTranslation(Data.getVersion().getID(), "version", new Object[]{Translation.getTranslation(Data.getVersion().getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.getVersion().getFriendlyString(), false, new Formatting[]{Formatting.WHITE})}), 2, this.height - 10, 0xFFFFFF);
		Text licenceText = Translation.getTranslation(Data.getVersion().getID(), "license", new Object[]{Translation.getTranslation(Data.getVersion().getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.getVersion().getFriendlyString(false), false, new Formatting[]{Formatting.WHITE})});
		context.drawTextWithShadow(textRenderer, licenceText, this.width - this.textRenderer.getWidth(licenceText) - 2, this.height - 10, 0xFFFFFF);
		getLogoWidget(this.width / 2 - 128, 30).renderWidget(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, getPageTitle(), this.width / 2, 78, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.getVersion().getID(), "warning.perspective", new Formatting[]{Formatting.RED, Formatting.BOLD}), ClientData.minecraft.getWindow().getScaledWidth() / 2, 10, 0xFFFFFFFF);
	}
	public Screen getRefreshScreen() {
		return this;
	}
	public Text getPageTitle() {
		return !getPageId().isEmpty() ? Translation.getConfigTranslation(Data.getVersion().getID(), getPageId()) : Text.empty();
	}
	public String getPageId() {
		return "";
	}
	public int getMaxPage() {
		return 1;
	}
	public int getGridMargin() {
		return 2;
	}
	public PerspectiveLogo.Widget getLogoWidget(int x, int y) {
		return new PerspectiveLogo.Widget(x, y);
	}
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);
		ClientData.minecraft.setScreen(getRefreshScreen());
	}
}
