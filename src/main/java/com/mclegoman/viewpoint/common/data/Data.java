/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.common.data;

import com.mclegoman.viewpoint.luminance.common.util.ModContainer;
import com.mclegoman.viewpoint.luminance.common.util.ModHelper;
import com.mclegoman.viewpoint.luminance.common.util.Version;

import java.util.Collections;
import java.util.Optional;

public class Data extends com.mclegoman.viewpoint.luminance.common.data.Data {
	private static final Version version;
	public static Version getVersion() {
		return version;
	}
	static {
		Optional<ModContainer> modContainer = ModHelper.getModContainer("viewpoint");
		version = Version.parse(modContainer.isPresent() ? modContainer.get().metadata() : new ModContainer.ModMetadata("viewpoint", "0.0.0-release.0", "Viewpoint", "metadata could not be found!", Collections.emptyList(), Collections.emptyList()), "SC58BGUF");
	}
}