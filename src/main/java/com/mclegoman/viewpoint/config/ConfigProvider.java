/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.config;

import com.mclegoman.viewpoint.common.data.Data;
import com.mclegoman.viewpoint.luminance.Couple;
import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import com.mclegoman.viewpoint.luminance.Version;
import com.mclegoman.viewpoint.magistermaks.SimpleConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigProvider implements SimpleConfig.DefaultConfig {
	private String contents = "";
	private List<Couple<String, ?>> configList = new ArrayList<>();
	public List<Couple<String, ?>> getConfigList() {
		return configList;
	}
	public void add(Couple<String, ?> keyValueSet) {
		configList.add(keyValueSet);
	}
	@Override
	public String get(String namespace) {
		return contents;
	}
	public void setContents(String id) {
		StringBuilder contents = new StringBuilder(Translation.getString("#{}.properties file\n", id));
		for (Couple<String, ?> option : configList) {
			contents.append(option.getFirst()).append("=").append(option.getSecond()).append("\n");
		}
		this.contents = contents.toString();
	}
	public void setConfig(String keyName, Object keyValue) {
		List<Couple<String, ?>> newConfigList = this.configList;
		for (Couple<String, ?> key : newConfigList) {
			int keyIndex = newConfigList.indexOf(key);
			if (key.getFirst().equals(keyName)) {
				newConfigList.set(keyIndex, new Couple<>(keyName, keyValue));
			}
		}
		configList = newConfigList;
	}
	public void saveConfig(Version data, String id) {
		try {
			setContents(id);
			PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getConfigDir().resolve(id + ".properties").toFile(), StandardCharsets.UTF_8);
			writer.write(contents);
			writer.close();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to save {} config: {}", id, error));
		}
	}
}