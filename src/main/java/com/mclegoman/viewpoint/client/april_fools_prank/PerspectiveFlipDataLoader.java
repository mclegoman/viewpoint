/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.april_fools_prank;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveFlipDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static List<String> registry = new ArrayList<>();
    public static final String ID = "flip";
    public PerspectiveFlipDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            registry.clear();
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, "flip.json"))) {
                for (JsonElement element : JsonHelper.getArray(JsonHelper.deserialize(resource.getReader()), "values", new JsonArray())) {
                    registry.add(element.getAsString());
                }
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.error("{}Failed to load reverse prank values: {}", PerspectiveData.PREFIX, error.getLocalizedMessage());
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
}