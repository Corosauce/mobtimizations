package com.corosus.mobtimizations;

import com.corosus.mobtimizations.Mobtimizations;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

import static net.minecraft.commands.Commands.literal;

public class CommandMisc {
	public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal(getCommandName()).requires(s -> s.hasPermission(2))
					.then(literal("toggle_on").executes( c -> {
								Mobtimizations.modActive = !Mobtimizations.modActive;
								c.getSource().sendSuccess(new TextComponent("Mod is now " + (Mobtimizations.modActive ? "active" : "inactive")), true);
								return Command.SINGLE_SUCCESS;
							})
					).then(literal("test_spawn_1000_zombies").executes( c -> {
								Mobtimizations.testSpawningActive = !Mobtimizations.testSpawningActive;
								c.getSource().sendSuccess(new TextComponent("Test spawning is now " + (Mobtimizations.testSpawningActive ? "active" : "inactive")), true);
								return Command.SINGLE_SUCCESS;
							})
					).then(literal("cancels").executes( c -> {
								c.getSource().sendSuccess(new TextComponent("Cancels " + (Mobtimizations.getCancels())), true);
								return Command.SINGLE_SUCCESS;
							})
					)
		);
	}

	public static String getCommandName() {
		return "mobtimizations";
	}
}
