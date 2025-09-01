/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.config;

import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalConfig {
    protected static final String ID = PerspectiveData.ID + "-experimental";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static void init() {
        try {
            CONFIG_PROVIDER = new PerspectiveConfigProvider();
            create();
            CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
            assign();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize {} config: {}", ID, error);
        }
    }
    protected static void create() {
    }
    protected static void assign() {
    }
    protected static void save() {
    }
}