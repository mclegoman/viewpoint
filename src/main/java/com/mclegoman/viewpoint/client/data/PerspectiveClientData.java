/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.data;

import com.mclegoman.viewpoint.client.config.PerspectiveConfigHelper;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

public class PerspectiveClientData {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static Identifier getLogo() {
        return PerspectiveData.IS_DEVELOPMENT ? getLogoType(PerspectiveData.ID, true, isPride()) : getLogoType(PerspectiveData.ID, false, isPride());
    }
    public static Identifier getLogoType(String namespace, boolean development, boolean pride) {
        return development ? new Identifier(namespace, (getLogoPath(pride) + "development.png")) : new Identifier(namespace, (getLogoPath(pride) + "release.png"));
    }
    public static String getLogoPath(boolean pride) {
        return pride ? "textures/logo/pride/" : "textures/logo/normal/";
    }
    public static boolean isPride() {
        if ((boolean) PerspectiveConfigHelper.getConfig("force_pride")) return true;
        else {
            LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
            return date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
        }
    }
}