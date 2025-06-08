/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.hud;

import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.events.Events;
import com.mclegoman.viewpoint.client.translation.Translation;
import com.mclegoman.viewpoint.client.util.IdentifierHelper;
import com.mclegoman.viewpoint.client.util.Mouse;
import com.mclegoman.viewpoint.client.util.Position;
import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.config.ConfigHelper;
import com.mclegoman.viewpoint.config.value.QualityToggle;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Overlays {
	public static boolean canRender;
	private static final List<String> timeOverlayTypes = new ArrayList<>();
	public static void init() {
		timeOverlayTypes.add("false");
		timeOverlayTypes.add("twelve_hour");
		timeOverlayTypes.add("twenty_four_hour");
		Mouse.ProcessCPS.register(Identifier.of(Data.version.getID(), "cps_overlay"), () -> (boolean)ConfigHelper.getConfig("cps_overlay"));
		createVariables();
	}
	private static void createVariables() {
		Events.Variables.register(Identifier.of(Data.version.getID(), "position"), (args) -> {
			if (ClientData.minecraft.player != null) {
				boolean integer = args[0] != null && args[0].equalsIgnoreCase("true");
				return Translation.getTranslation(Data.version.getID(), "position.description", new Object[]{Position.getX(ClientData.minecraft.player.getPos(), integer), Position.getY(ClientData.minecraft.player.getPos(), integer), Position.getZ(ClientData.minecraft.player.getPos(), integer)});
			}
			return Text.literal("?");
		});
		Events.Variables.register(Identifier.of(Data.version.getID(), "time"), (args) -> {
			if (ClientData.minecraft.world != null) {
				long time = ClientData.minecraft.world.getTimeOfDay() % 24000L;
				int rawHour = (int) (time / 1000 + 6) % 24;
				int rawMinute = (int) (time / 16.666666) % 60;
				String hour = String.valueOf(rawHour);
				String minute = String.valueOf(rawMinute < 10 ? "0" + rawMinute : rawMinute);
				Text suffix = Text.empty();
				if (args[0] != null && args[0].equalsIgnoreCase("twelve_hour")) {
					hour = String.valueOf(rawHour == 0 || rawHour == 12 ? 12 : rawHour % 12);
					if (rawHour < 10 && rawHour != 0) hour = "0" + hour;
					suffix = rawHour < 12 ? Translation.getTranslation(Data.version.getID(), "time_overlay.am") : Translation.getTranslation(Data.version.getID(), "time_overlay.pm");
				}
				return Translation.getTranslation(Data.version.getID(), "time", new Object[]{hour, minute, suffix});
			}
			return Text.literal("?");
		});
		Events.Variables.register(Identifier.of(Data.version.getID(), "day"), (args) -> Text.literal(String.valueOf(ClientData.minecraft.world != null ? ClientData.minecraft.world.getTimeOfDay() / 24000L : "?")));
		Events.Variables.register(Identifier.of(Data.version.getID(), "biome"), (args) -> {
			String biome = ClientData.minecraft.player != null && ClientData.minecraft.world != null ? ClientData.minecraft.world.getBiome(ClientData.minecraft.player.getBlockPos()).getKeyOrValue().map((biomeKey) -> biomeKey.getValue().toString(), (biome_) -> "[unregistered " + biome_ + "]") : null;
			return biome != null ? Text.translatable("biome." + IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, biome) + "." + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, biome)) : Text.literal("?");
		});
		Events.Variables.register(Identifier.of(Data.version.getID(), "stat_used"), (args) -> Text.literal(args[0] != null && ClientData.minecraft.player != null ? String.valueOf(ClientData.minecraft.player.getStatHandler().getStat(Stats.USED.getOrCreateStat(Registries.ITEM.get(Identifier.of(args[0]))))) : "?"));
		Events.Variables.register(Identifier.of(Data.version.getID(), "stat_custom"), (args) -> Text.literal(args[0] != null && ClientData.minecraft.player != null ? String.valueOf(ClientData.minecraft.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Registries.CUSTOM_STAT.get(Identifier.of(args[0]))))) : "?"));
		Events.Variables.register(Identifier.of(Data.version.getID(), "cps_left"), (args) -> Text.literal(String.valueOf(Mouse.getLeftCPS())));
		Events.Variables.register(Identifier.of(Data.version.getID(), "cps_middle"), (args) -> Text.literal(String.valueOf(Mouse.getMiddleCPS())));
		Events.Variables.register(Identifier.of(Data.version.getID(), "cps_right"), (args) -> Text.literal(String.valueOf(Mouse.getRightCPS())));
		Events.Variables.register(Identifier.of(Data.version.getID(), "looking_at"), (args) -> getLookingAt((LivingEntity) ClientData.minecraft.cameraEntity));
	}
	public static void updateStats() {
		if (ClientData.minecraft.getNetworkHandler() != null) ClientData.minecraft.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS));
	}
	public static String getCurrentTimeOverlay() {
		return (String) ConfigHelper.getConfig("time_overlay");
	}
	public static boolean isValidTimeOverlay(String timeOverlay) {
		return timeOverlayTypes.contains(timeOverlay);
	}
	public static void cycleTimeOverlay(boolean direction) {
		int currentIndex = timeOverlayTypes.indexOf(getCurrentTimeOverlay());
		ConfigHelper.setConfig("time_overlay", timeOverlayTypes.get(direction ? (currentIndex + 1) % timeOverlayTypes.size() : (currentIndex - 1 + timeOverlayTypes.size()) % timeOverlayTypes.size()));
	}
	public static Text getEntityPositionTextTitle() {
		return Translation.getTranslation(Data.version.getID(), "position.title");
	}
	public static Text getEntityPositionTextDescription(Vec3d pos) {
		return Translation.getTranslation(Data.version.getID(), "position.description", new Object[]{
				Position.getX(pos, true),
				Position.getY(pos, true),
				Position.getZ(pos, true),
		});
	}
	public static void renderOverlays(DrawContext context) {
		if (!ClientData.minecraft.getDebugHud().shouldShowDebugHud() && !ClientData.minecraft.options.hudHidden && !HUDHelper.shouldHideHUD()) {
			// Version Overlay
			if ((boolean) ConfigHelper.getConfig("version_overlay"))
				context.drawTextWithShadow(ClientData.minecraft.textRenderer, Translation.getTranslation(Data.version.getID(), "version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
			// Other Overlays
			int y = 40;
			List<Text> overlayTexts = new ArrayList<>();
			if ((boolean) ConfigHelper.getConfig("position_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "position_overlay") + "](Translatable[" + Translation.getTranslationKey(Data.version.getID(), "position.title") + "]," + "Variable[" + Data.version.getID() + ":position](true))"));
			}
			if (!((String)(ConfigHelper.getConfig("time_overlay"))).equalsIgnoreCase("false")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "time_overlay") + "](Variable[" + Data.version.getID() + ":time](" + (String)ConfigHelper.getConfig("time_overlay") + "))"));
			}
			if ((boolean) ConfigHelper.getConfig("day_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "day_overlay") + "](Variable[" + Data.version.getID() + ":day])"));
			}
			if ((boolean) ConfigHelper.getConfig("biome_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "biome_overlay") + "](Variable[" + Data.version.getID() + ":biome])"));
			}
			if ((boolean) ConfigHelper.getConfig("deaths_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "deaths_overlay") + "](Variable[" + Data.version.getID() + ":stat_custom](" + Stats.DEATHS + "))"));
			}
			if ((boolean) ConfigHelper.getConfig("totems_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "totems_overlay") + "](Variable[" + Data.version.getID() + ":stat_used](minecraft:totem_of_undying))"));
			}
			if ((boolean) ConfigHelper.getConfig("cps_overlay")) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "cps_overlay") + "](Variable[" + Data.version.getID() + ":cps_left],Variable[" + Data.version.getID() + ":cps_middle],Variable[" + Data.version.getID() + ":cps_right])"));
			}
			if (!((String) ConfigHelper.getConfig("looking_at_overlay")).equals(QualityToggle.off.asString())) {
				overlayTexts.add(Translation.getParsedTextFromString("Translatable[" + Translation.getTranslationKey(Data.version.getID(), "looking_at_overlay") + "](Variable[" + Data.version.getID() + ":looking_at])"));
			}
			renderOverlays(context, overlayTexts, 0, y, false);
		}
	}
	public static void renderOverlay(DrawContext context, int x, int y, Text text) {
		renderOverlay(context, x, y, text, -1873784752, 0xffffff, false);
	}
	public static void renderOverlay(DrawContext context, int x, int y, Text text, int backgroundColor, int textColor, boolean shadow) {
		context.fill(x, y, x + ClientData.minecraft.textRenderer.getWidth(text) + 4, y + 12, backgroundColor);
		context.drawText(ClientData.minecraft.textRenderer, text, x + 2, y + 2, textColor, shadow);
	}
	public static void renderOverlays(DrawContext context, List<Text> overlays, int x, int y, boolean wrap) {
		renderOverlays(context, overlays, x, y, wrap, 0);
	}
	public static void renderOverlays(DrawContext context, List<Text> overlays, int x, int y, boolean wrap, int wrapY) {
		int wrapX = 0;
		for (Text overlay : overlays) {
			if (!overlay.equals(Text.empty())) {
				wrapX = Math.max(wrapX, ClientData.minecraft.textRenderer.getWidth(overlay));
				if (wrap && (y > ClientData.minecraft.getWindow().getScaledHeight() - 2 - 9)) {
					y = wrapY;
					x += (wrapX + 4);
				}
				Overlays.renderOverlay(context, x, y, overlay);
			}
			y = HUDHelper.addY(y);
		}
	}





	public static Text getLookingAt(LivingEntity entity) {
		if (entity != null) {
			HitResult hitResult = ClientData.minecraft.crosshairTarget;
			if (hitResult != null) {
				switch (hitResult.getType()) {
					case ENTITY -> {
						return getLookingAtEntity((EntityHitResult) hitResult, QualityToggle.valueOf(((String)ConfigHelper.getConfig("looking_at_overlay"))), entity);
					} case BLOCK -> {
						return getBlock((BlockHitResult) hitResult, QualityToggle.valueOf(((String)ConfigHelper.getConfig("looking_at_overlay"))), entity);
					}
				}
			}
		}
		return getLookingAtFallbackText();
	}
	private static Text getLookingAtEntity(EntityHitResult hitResult, QualityToggle quality, LivingEntity entity) {
		switch (quality) {
			case fast -> {
				return hitResult.getEntity().getType().getName();
			}
			case fancy -> {
				Optional<Text> variant = getLookingAtEntityVariant(hitResult.getEntity());
				return Translation.getCombinedText((MutableText) variant.orElse(Text.empty()), (variant.isPresent() ? Text.literal(" ") : Text.empty()), (MutableText) hitResult.getEntity().getType().getName());
			}
		}
		return getLookingAtFallbackText();
	}
	private static Optional<Text> getLookingAtEntityVariant(Entity entity) {
		switch (entity) {
			case ParrotEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case FoxEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case PaintingEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().getIdAsString().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case RabbitEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case VillagerEntity holderEntity -> {
				String profession = holderEntity.getVillagerData().getProfession().id().toLowerCase();
				Identifier variant = Identifier.of(holderEntity.getVariant().toString().toLowerCase());
				Text text = Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath())), !profession.equals("none") ? Text.literal(" ") : Text.empty(), !profession.equals("none") ? Text.translatable("merchant.level." + holderEntity.getVillagerData().getLevel()) : Text.empty(), !profession.equals("none") ? Text.literal(" ") : Text.empty(), !profession.equals("none") ? Text.translatableWithFallback(holderEntity.getType().getTranslationKey() + ".profession", Translation.getTitleCase(profession)) : Text.empty());
				return Optional.of(text);
			}
			case MooshroomEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case HorseEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case SalmonEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case LlamaEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case AxolotlEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case FrogEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().getIdAsString().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case WolfEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().getIdAsString().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case CatEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getVariant().getIdAsString().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			case TropicalFishEntity holderEntity -> {
				return Optional.of(Translation.getCombinedText((MutableText) holderEntity.getVariant().getText()));
			}
			case ShulkerEntity holderEntity -> {
				Identifier variant = holderEntity.getColor() != null ? Identifier.of(holderEntity.getColor().name().toLowerCase()) : null;
				return variant != null ? Optional.of(Translation.getCombinedText(variant != null ? Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath())) : Text.empty())) : Optional.empty();
			}
			case SheepEntity holderEntity -> {
				Identifier variant = Identifier.of(holderEntity.getColor().name().toLowerCase());
				return Optional.of(Translation.getCombinedText(Text.translatableWithFallback(getLookingAtIdVariantTranslationKey(holderEntity, variant), Translation.getTitleCase(variant.getPath()))));
			}
			default -> {
			}
		}
		return Optional.empty();
	}
	private static String getLookingAtIdVariantTranslationKey(Entity entity, Identifier variant) {
		return variant.toTranslationKey(entity.getType().getTranslationKey() + ".variant");
	}
	private static Text getBlock(BlockHitResult hitResult, QualityToggle quality, LivingEntity entity) {
		switch (quality) {
			case fast -> {
				return entity.getWorld().getBlockState(hitResult.getBlockPos()).getBlock().getName();
			}
			case fancy -> {
				return entity.getWorld().getBlockState(((BlockHitResult)entity.raycast(entity.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE), ClientData.minecraft.getRenderTickCounter().getTickDelta(true), true)).getBlockPos()).getBlock().getName();
			}
		}
		return getLookingAtFallbackText();
	}
	private static Text getLookingAtFallbackText() {
		return Translation.getTranslation(Data.version.getID(), "looking_at_overlay.none");
	}
}