/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance;

import net.minecraft.util.Identifier;

public class IdentifierHelper {
	public static Identifier identifierFromString(String identifier) {
		return Identifier.of(identifier);
	}
	public static String stringFromIdentifier(Identifier identifier) {
		return identifier.toString();
	}
	public static String getStringPart(Type type, String identifier) {
		return switch (type) {
			case NAMESPACE -> identifierFromString(identifier).getNamespace();
			case KEY -> identifierFromString(identifier).getPath();
		};
	}
	public static String getIdentifierPart(Type type, Identifier identifier) {
		return switch (type) {
			case NAMESPACE -> identifier.getNamespace();
			case KEY -> identifier.getPath();
		};
	}
	public enum Type {
		NAMESPACE,
		KEY
	}
}