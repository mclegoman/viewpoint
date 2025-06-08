/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.config.value;

import net.minecraft.util.StringIdentifiable;

public enum QualityToggle implements StringIdentifiable {
	off("off"),
	fast("fast"),
	fancy("fancy");
	private final String name;
	QualityToggle(String name) {
		this.name = name;
	}
	public String asString() {
		return this.name;
	}
}