/*
    viewpoint
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/viewpoint
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.common.data;

import com.mclegoman.viewpoint.luminance.LogType;
import com.mclegoman.viewpoint.luminance.Translation;
import com.mclegoman.viewpoint.luminance.Version;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.commons.lang3.StringUtils;

public class Data {
	public static final Version version = Version.parse(FabricLoader.getInstance().getModContainer("viewpoint").get().getMetadata(), "SC58BGUF");
	public static boolean isModInstalled(String modId) {
		try {
			return FabricLoader.getInstance().isModLoaded(modId);
		} catch (Exception ignored) {
			return false;
		}
	}
	public static boolean isModInstalledVersionOrHigher(String modId, String requiredVersion, boolean substring, String separator) {
		try {
			if (isModInstalled(modId)) {
				return checkModVersion(getModContainer(modId).getMetadata().getVersion().getFriendlyString(), requiredVersion, substring);
			}
		} catch (Exception error) {
			version.sendToLog(LogType.ERROR, Translation.getString("Failed to check mod version for " + modId + ": {}", error));
		}
		return false;
	}
	public static boolean isModInstalledVersionOrHigher(String modId, String requiredVersion, boolean substring) {
		return isModInstalledVersionOrHigher(modId, requiredVersion, substring, "-");
	}
	public static boolean isModInstalledVersionOrHigher(String modId, String requiredVersion) {
		return isModInstalledVersionOrHigher(modId, requiredVersion, false);
	}
	public static boolean checkModVersion(String currentVersion, String requiredVersion, boolean substring, String separator) {
		try {
			return net.fabricmc.loader.api.Version.parse(requiredVersion).compareTo(net.fabricmc.loader.api.Version.parse(substring ? StringUtils.substringBefore(currentVersion, separator) : currentVersion)) <= 0;
		} catch (Exception error) {
			version.sendToLog(LogType.ERROR, Translation.getString("Failed to check mod version!"));
		}
		return false;
	}
	public static boolean checkModVersion(String currentVersion, String requiredVersion, boolean substring) {
		return checkModVersion(currentVersion, requiredVersion, substring, "-");
	}
	public static ModContainer getModContainer(String modId) {
		return FabricLoader.getInstance().getModContainer(modId).get();
	}
}