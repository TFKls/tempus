package dev.tfkls.tempus.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import dev.tfkls.tempus.managers.TemperatureManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

import static net.minecraft.server.command.CommandManager.*;

public class TemperatureCommand implements CommandRegistrationCallback {
	public static void register() {
		CommandRegistrationCallback.EVENT.register(new TemperatureCommand());
	}

	/**
	 * Called when the server is registering commands.
	 *
	 * @param dispatcher     the command dispatcher to register commands to
	 * @param registryAccess object exposing access to the game's registries
	 * @param environment    environment the registrations should be done for, used for commands that are dedicated or integrated server only
	 */

	// TODO -- currently crashes on non-null "targets" (works only on command's source), needs to be debugged
	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
		dispatcher.register(literal("temperature")
				.requires(source -> source.hasPermissionLevel(2))
				.then(literal("clear")
						.executes(context -> executeClear(context.getSource(), List.of(context.getSource().getPlayerOrThrow())))
						.then(argument("targets", EntityArgumentType.players()).executes(context -> executeClear(context.getSource(), context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource())))))
				.then(literal("query")
						.executes(context -> executeQuery(context.getSource(), List.of(context.getSource().getPlayerOrThrow())))
						.then(argument("targets", EntityArgumentType.players()).executes(context -> executeQuery(context.getSource(), context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource())))))
				.then(literal("set")
						.then(argument("value", FloatArgumentType.floatArg())
								.executes(context -> executeSet(context.getSource(), context.getArgument("value", float.class), false, List.of(context.getSource().getPlayerOrThrow())))
								.then(argument("targets", EntityArgumentType.players())
										.executes(context -> executeSet(context.getSource(), context.getArgument("value", float.class), false, context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource()))))))
				.then(literal("add")
						.then(argument("value", FloatArgumentType.floatArg())
								.executes(context -> executeSet(context.getSource(), context.getArgument("value", float.class), true, List.of(context.getSource().getPlayerOrThrow())))
								.then(argument("targets", EntityArgumentType.players())
										.executes(context -> executeSet(context.getSource(), context.getArgument("value", float.class), true, context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource()))))))
		);
	}

	private static int executeClear(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
		return executeSet(source, 0f, false, players);
	}

	private static int executeQuery(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
		for (PlayerEntity player : players) {
			TemperatureManager manager = ((TemperatureManager.MixinAccessor) player).tempus$getTemperatureManager();
			source.sendFeedback(() -> Text.translatable("command.tempus.temperature.query", player.getDisplayName(), String.valueOf(manager.getTemperature())), true);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int executeSet(ServerCommandSource source, float value, boolean isAdd, Collection<ServerPlayerEntity> players) {
		for (PlayerEntity player : players) {
			TemperatureManager manager = ((TemperatureManager.MixinAccessor) player).tempus$getTemperatureManager();
			float goal = (isAdd ? manager.getTemperature() : 0) + value;
			manager.applyUninsulatedSingular(0, 1f);
			manager.applyUninsulatedSingular(value, 1f);
		}
		if (players.size() == 1) {
			PlayerEntity player = players.iterator().next();
			TemperatureManager manager = ((TemperatureManager.MixinAccessor) player).tempus$getTemperatureManager();
			source.sendFeedback(() -> Text.translatable("command.tempus.temperature.set.single", player.getDisplayName(), String.valueOf(manager.getTemperature())), true);
		} else {
			source.sendFeedback(() -> Text.translatable("command.tempus.temperature.set.multiple", players.size(), (isAdd ? "by" : "to"), String.valueOf(value)), true);
		}
		return Command.SINGLE_SUCCESS;
	}
}

