package com.mclegoman.viewpoint.mixin.compat;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.screen.config.ConfigScreen;
import com.mclegoman.viewpoint.client.screen.widget.LogoButtonWidget;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.common.data.Data;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = SodiumOptionsGUI.class, remap = false)
public class SodiumOptionsGUIMixin extends Screen {
    @Unique
    private ButtonWidget perspective$widget;
    protected SodiumOptionsGUIMixin(Text title) {
        super(title);
    }
    @Inject(method = "rebuildGUI", at = @At("RETURN"))
    protected void viewpoint$addButton(CallbackInfo ci) {
        perspective$widget = LogoButtonWidget.create(() -> {
            String path = FabricLoader.getInstance().getModContainer(Data.getVersion().getID()).get().getMetadata().getIconPath(64).orElse(null);
            if (path != null) {
                path = path.replaceFirst("assets/", "").replaceFirst("/", ":");
            }
            return path != null ? Identifier.of(path) : null;
        }, (button) -> ClientData.minecraft.setScreen(new ConfigScreen(this, 1))).width(20).position(this.width - 28, this.height - 54).tooltip(Tooltip.of(Translation.getTranslation(Data.getVersion().getID(), "config", new Object[]{Translation.getTranslation(Data.getVersion().getID(), "name"), Translation.getConfigTranslation(Data.getVersion().getID(), "config")}))).build();
        this.addDrawableChild(perspective$widget);
    }
}
