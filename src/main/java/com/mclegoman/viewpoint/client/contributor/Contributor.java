/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.contributor;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Contributor {
	public static List<ContributorData> contributors = new ArrayList<>();
	// This is a very slimmed down version of Perspective's contributor system, with no dataloader and some features removed.
	public static void init() {
		addLego("772eb47b-a24e-4d43-a685-6ca9e9e132f7", "3445ebd7-25f8-41a6-8118-0d19d7f5559e");
	}
	private static void addLego(String... uuids) {
		for (String uuid : uuids) contributors.add(ContributorData.builder(uuid).shouldFlipUpsideDown(true).shouldReplaceCape(true).capeTexture(Identifier.of("perspective", "textures/contributors/cape/dev_oneyear.png")).type(Type.DEVELOPER.name).build());
	}
	public enum Type {
		DEVELOPER("developer"),
		CONTRIBUTOR("contributor"),
		ATTRIBUTION("attribution");
		private final String name;
		Type(String name) {
			this.name = name;
		}
		public String getName() {
			return this.name;
		}
	}
}