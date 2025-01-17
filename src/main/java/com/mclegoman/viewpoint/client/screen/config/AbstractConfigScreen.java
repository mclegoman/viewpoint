/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.screen.config;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.keybindings.Keybindings;
import com.mclegoman.viewpoint.client.screen.LogoWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.luminance.LogType;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractConfigScreen extends Screen {
	protected int page;
	protected final Screen parentScreen;
	protected final GridWidget grid;
	protected GridWidget.Adder gridAdder;
	protected boolean refresh;
	protected boolean shouldClose;
	protected boolean saveOnClose;
	public AbstractConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(Text.literal(""));
		this.grid = new GridWidget();
		this.parentScreen = parentScreen;
		this.refresh = refresh;
		this.saveOnClose = saveOnClose;
		this.page = page;
	}
	public void init() {
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
			if (this.refresh) ClientData.minecraft.setScreen(getRefreshScreen());
			if (this.shouldClose) {
				if (saveOnClose()) ConfigHelper.saveConfig();
				ClientData.minecraft.setScreen(this.parentScreen);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick perspective$config screen: {}", error));
		}
	}
	protected GridWidget createFooter() {
		GridWidget footerGrid = new GridWidget();
		footerGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder footerGridAdder = footerGrid.createAdder(3);
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "reset"), (button) -> {
			if (ConfigHelper.resetConfig()) this.refresh = true;
		}).build());
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> {
			if (this.page <= 1) {
				this.shouldClose = true;
			}
			else {
				this.page -= 1;
				this.refresh = true;
			}
		}).width(73).build());
		ButtonWidget nextButtonWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "next"), (button) -> {
			if (!(this.page >= getMaxPage())) {
				this.page += 1;
				this.refresh = true;
			}
		}).width(73).build();
		if (this.page >= getMaxPage()) nextButtonWidget.active = false;
		footerGridAdder.add(nextButtonWidget);
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
			}
			else {
				this.page -= 1;
				this.refresh = true;
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_F5 && hasControlDown()) {
			ConfigHelper.reloadConfig(true);
			this.refresh = true;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (ConfigHelper.showReloadOverlay) context.drawTextWithShadow(textRenderer, Translation.getConfigTranslation(Data.version.getID(), "reload"), this.width - textRenderer.getWidth(Translation.getConfigTranslation(Data.version.getID(), "reload")) - 2, 2, 0xFFFFFF);
		context.drawTextWithShadow(textRenderer, Translation.getTranslation(Data.version.getID(), "version", new Object[]{Translation.getTranslation(Data.version.getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.version.getFriendlyString(), false, new Formatting[]{Formatting.WHITE})}), 2, this.height - 10, 0xFFFFFF);
		Text licenceText = Translation.getTranslation(Data.version.getID(), "license", new Object[]{Translation.getTranslation(Data.version.getID(), "name", new Formatting[]{Formatting.WHITE}), Translation.getText(Data.version.getFriendlyString(false), false, new Formatting[]{Formatting.WHITE})});
		context.drawTextWithShadow(textRenderer, licenceText, this.width - this.textRenderer.getWidth(licenceText) - 2, this.height - 10, 0xFFFFFF);
		getLogoWidget(this.width / 2 - 128, 30).renderWidget(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, getPageTitle(), this.width / 2, 78, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, com.mclegoman.viewpoint.luminance.Translation.getText("gui.viewpoint.perspective.fork", true, new Formatting[]{Formatting.RED, Formatting.BOLD}), this.width / 2, 2, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, com.mclegoman.viewpoint.luminance.Translation.getText("gui.viewpoint.perspective.use", true, new Formatting[]{Formatting.RED, Formatting.BOLD}), this.width / 2, 12, 0xFFFFFF);
	}
	public Screen getRefreshScreen() {
		return this;
	}
	public Text getPageTitle() {
		return !getPageId().isEmpty() ? Translation.getConfigTranslation(Data.version.getID(), getPageId()) : Text.empty();
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
	public boolean getExperimental() {
		return false;
	}
	public LogoWidget getLogoWidget(int x, int y) {
		return new LogoWidget(x, y, getLogoTexture());
	}
	public Identifier getLogoTexture() {
		return Identifier.of("viewpoint:textures/logo.png");
	}
	public boolean saveOnClose() {
		return this.saveOnClose;
	}
}
