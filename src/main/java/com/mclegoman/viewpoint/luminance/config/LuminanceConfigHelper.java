/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.config;

import com.mclegoman.viewpoint.luminance.config.serializers.LuminanceSerializer;
import com.mclegoman.viewpoint.client.data.ClientData;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.impl.ConfigImpl;
import org.quiltmc.config.implementor_api.ConfigEnvironment;

import java.io.File;
import java.nio.file.Path;

public class LuminanceConfigHelper {
	private static final ConfigEnvironment configEnvironment = getConfigEnvironment();
	public static <C extends ReflectiveConfig> C register(String namespace, String id, Class<C> config) {
		return ConfigImpl.createReflective(configEnvironment, namespace, id, Path.of(""), builder -> {}, config, builder -> {});
	}
	public static <C extends ReflectiveConfig> void reset(C config) {
		reset(config, true);
	}
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <C extends ReflectiveConfig> void reset(C config, boolean save) {
		for (TrackedValue value : config.values()) value.setValue(value.getDefaultValue(), false);
		if (save) config.save();
	}
	public static ConfigEnvironment getConfigEnvironment() {
		ConfigEnvironment propertiesConfigEnvironment;
		propertiesConfigEnvironment = new ConfigEnvironment(new File(ClientData.minecraft.runDirectory, "config").toPath(), "properties", new LuminanceSerializer("properties"));
		propertiesConfigEnvironment.registerSerializer(new LuminanceSerializer("properties"));
		return propertiesConfigEnvironment;
	}
	public enum SerializerType {
		TOML,
		PROPERTIES,
		JSON5
	}
}
