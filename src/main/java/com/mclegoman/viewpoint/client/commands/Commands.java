package com.mclegoman.viewpoint.client.commands;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import com.mclegoman.viewpoint.client.panorama.Panorama;
import com.mclegoman.viewpoint.common.util.Identifiers;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Commands {
    public static void init() {
        register((dispatcher, registryAccess) -> {
            dispatcher.register(literal(Identifiers.COMMAND_PANORAMA.toString())
                    .executes(context -> {
                        Panorama.takePanorama(PerspectiveConfig.config.panoramaResolution.value(), 4);
                        return 1;
                    }).then(argument("resourcePackName", StringArgumentType.string())
                            .executes(context -> {
                                Panorama.takePanorama(StringArgumentType.getString(context, "resourcePackName"), PerspectiveConfig.config.panoramaResolution.value(), 4);
                                return 1;
                            }).then(argument("resolution", IntegerArgumentType.integer(1))
                                    .executes(context -> {
                                        Panorama.takePanorama(StringArgumentType.getString(context, "resourcePackName"), IntegerArgumentType.getInteger(context, "resolution"), 4);
                                        return 1;
                                    }).then(argument("downscaleFactor", IntegerArgumentType.integer(1))
                                            .executes(context -> {
                                                Panorama.takePanorama(StringArgumentType.getString(context, "resourcePackName"), IntegerArgumentType.getInteger(context, "resolution"), IntegerArgumentType.getInteger(context, "downscaleFactor"));
                                                return 1;
                                            }).then(argument("description", StringArgumentType.string())
                                                    .executes(context -> {
                                                        Panorama.takePanorama(StringArgumentType.getString(context, "resourcePackName"), StringArgumentType.getString(context, "description"), IntegerArgumentType.getInteger(context, "resolution"), IntegerArgumentType.getInteger(context, "downscaleFactor"));
                                                        return 1;
                                                    })
                                            )
                                    )
                            )
                    )
            );
        });
    }
    private static void register(ClientCommandRegistrationCallback command) {
        ClientCommandRegistrationCallback.EVENT.register(command);
    }
}
