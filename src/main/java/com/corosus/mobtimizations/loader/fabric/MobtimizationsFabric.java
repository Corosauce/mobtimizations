package com.corosus.mobtimizations.loader.fabric;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.CommandMisc;
import com.corosus.mobtimizations.config.MobListsConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.io.File;

public class MobtimizationsFabric extends Mobtimizations implements ModInitializer {

	public static MinecraftServer minecraftServer = null;

	public MobtimizationsFabric() {
		super();

		ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, MobListsConfig.CONFIG, Mobtimizations.MODID + File.separator + "MobsBlacklist.toml");
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
			MobtimizationsFabric.minecraftServer = minecraftServer;
		});

		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> CommandMisc.register(dispatcher)));
	}
}