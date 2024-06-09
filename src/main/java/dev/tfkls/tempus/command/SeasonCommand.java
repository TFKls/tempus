package dev.tfkls.tempus.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.tfkls.tempus.managers.SeasonManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SeasonCommand implements CommandRegistrationCallback {
	public static void register() {
		CommandRegistrationCallback.EVENT.register(new SeasonCommand());
	}

	/**
	 * Called when the server is registering commands.
	 *
	 * @param dispatcher     the command dispatcher to register commands to
	 * @param registryAccess object exposing access to the game's registries
	 * @param environment    environment the registrations should be done for, used for commands that are dedicated or integrated server only
	 */
	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(literal("season")
				.requires(source -> source.hasPermissionLevel(2))
				.then(literal("query").executes(SeasonCommand::executeQuery))
				.then(literal("set")
						.then(argument("offset", IntegerArgumentType.integer())
								.executes(SeasonCommand::executeSet)))
				.then(literal("add")
						.then(argument("offset", IntegerArgumentType.integer())
								.executes(SeasonCommand::executeAdd))));
	}

	private static int executeQuery(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(() -> Text.translatable("command.tempus.season.query", SeasonManager.getInstance().currentSeason()), false);
		return Command.SINGLE_SUCCESS;
	}

	private static int executeSet(CommandContext<ServerCommandSource> context) {
		SeasonManager manager = SeasonManager.getInstance();
		manager.setSeasonOffset(context.getArgument("offset", int.class) - (manager.currentSeason() - manager.getSeasonOffset()));
		context.getSource().sendFeedback(() -> Text.translatable("command.tempus.season.set", manager.getSeasonOffset(), manager.currentSeason()), true);
		return Command.SINGLE_SUCCESS;
	}

	private static int executeAdd(CommandContext<ServerCommandSource> context) {
		SeasonManager manager = SeasonManager.getInstance();
		manager.setSeasonOffset(manager.getSeasonOffset() + context.getArgument("offset", int.class));
		context.getSource().sendFeedback(() -> Text.translatable("command.tempus.season.add", manager.getSeasonOffset(), context.getArgument("offset", int.class)), true);
		return Command.SINGLE_SUCCESS;
	}
}
