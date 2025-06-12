/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.common.data;

import com.mclegoman.viewpoint.luminance.client.translation.Translation;
import com.mclegoman.viewpoint.luminance.common.util.LogType;
import com.mclegoman.viewpoint.luminance.common.util.ModHelper;
import com.mclegoman.viewpoint.luminance.common.util.ModMetadata;
import com.mclegoman.viewpoint.luminance.common.util.Version;
import net.fabricmc.loader.api.ModContainer;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Optional;

public class Data {
	private static final Version version;
	public static Version getVersion() {
		return version;
	}
	public static boolean isModInstalled(String modId) {
		try {
			return ModHelper.isModLoaded(modId);
		} catch (Exception ignored) {
			return false;
		}
	}
	public static boolean isModInstalledVersionOrHigher(String modId, String requiredVersion, boolean substring, String separator) {
		try {
			if (isModInstalled(modId)) {
				Optional<ModMetadata> modContainer = ModHelper.getModMetadata(modId);
				if (modContainer.isPresent()) return checkModVersion(modContainer.get().rawVersion(), requiredVersion, substring);
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
	static {
		version = Version.parse(ModHelper.getModMetadata("viewpoint").orElseGet(() -> new ModMetadata("viewpoint", "0.0.0-release.0", "Viewpoint", "metadata could not be found!", Collections.emptyList(), Collections.emptyList())), "SC58BGUF");
	}
}