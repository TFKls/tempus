package dev.tfkls.tempus.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.tfkls.tempus.managers.NutritionManager;
import dev.tfkls.tempus.misc.NutritionType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.*;

public class NutritionCommand implements CommandRegistrationCallback {
	public static void register() {
		ArgumentTypeRegistry.registerArgumentType(
				new Identifier("tempus", "nutrition"),
				NutritionArgumentType.class,
				ConstantArgumentSerializer.of(NutritionArgumentType::new)
		);
		CommandRegistrationCallback.EVENT.register(new NutritionCommand());
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
		dispatcher.register(literal("nutrition")
				.requires(source -> source.hasPermissionLevel(2))
				.then(literal("clear")
						.executes(context -> executeClear(context.getSource(), List.of(context.getSource().getPlayerOrThrow())))
						.then(argument("targets", EntityArgumentType.players()).executes(context -> executeClear(context.getSource(), context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource())))))
				.then(literal("query")
						.executes(context -> executeQuery(context.getSource(), List.of(context.getSource().getPlayerOrThrow())))
						.then(argument("targets", EntityArgumentType.players()).executes(context -> executeQuery(context.getSource(), context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource())))))
				.then(literal("set")
						.then(argument("nutrition", new NutritionArgumentType())
								.then(argument("level", IntegerArgumentType.integer(1, 10))
										.executes(context -> executeSet(context.getSource(), context.getArgument("nutrition", NutritionType.class), context.getArgument("level", Integer.class), List.of(context.getSource().getPlayerOrThrow())))
										.then(argument("targets", EntityArgumentType.players())
												.executes(context -> executeSet(context.getSource(), context.getArgument("nutrition", NutritionType.class), context.getArgument("level", Integer.class), context.getArgument("targets", EntitySelector.class).getPlayers(context.getSource()))))))
				)
		);
	}

	private static int executeClear(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
		return executeSet(source, NutritionType.NONE, 0, players);
	}

	private static int executeQuery(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
		for (PlayerEntity player : players) {
			NutritionManager manager = ((NutritionManager.MixinAccessor) player.getHungerManager()).tempus$getNutritionManager();
			source.sendFeedback(() -> nutritionToText(Text.translatable("command.tempus.nutrition.query", player.getDisplayName()), manager.getNutritionType(), manager.getNutritionLevel()), true);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int executeSet(ServerCommandSource source, NutritionType type, int level, Collection<ServerPlayerEntity> players) {
		for (PlayerEntity player : players) {
			((NutritionManager.MixinAccessor) player.getHungerManager()).tempus$getNutritionManager().setState(type, level);
		}
		if (players.size() == 1) {
			PlayerEntity player = players.iterator().next();
			NutritionManager manager = ((NutritionManager.MixinAccessor) player.getHungerManager()).tempus$getNutritionManager();
			source.sendFeedback(() -> nutritionToText(Text.translatable("command.tempus.nutrition.set.single", player.getDisplayName()), manager.getNutritionType(), manager.getNutritionLevel()), true);
		} else {
			source.sendFeedback(() -> nutritionToText(Text.translatable("command.tempus.nutrition.set.multiple", players.size()).copy(), type, level), true);
		}
		return Command.SINGLE_SUCCESS;
	}

	static class NutritionArgumentType implements ArgumentType<NutritionType> {

		@Override
		public NutritionType parse(StringReader reader) throws CommandSyntaxException {
			if (!reader.canRead()) {
				reader.skip();
			}
			String maybeType = reader.readUnquotedString();
			for (var type : NutritionType.values()) {
				if (type == NutritionType.NONE) continue;
				if (maybeType.equals(type.toString().toLowerCase())) {
					return type;
				}
			}
			throw new DynamicCommandExceptionType(value -> Text.translatable("command.tempus.nutrition.invalid", value.toString().toLowerCase())).create(maybeType);
		}

		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			for (var type : List.of("carbohydrate", "fat", "protein")) {
				if (CommandSource.shouldSuggest(builder.getRemaining(), type)) {
					builder.suggest(type);
				}
			}
			return builder.buildFuture();
		}
	}

	// TODO: add color formatting
	private static MutableText nutritionToText(MutableText appendTo, NutritionType type, int level) {
		appendTo.append(type.toString().toLowerCase());
		appendTo.append(" (" + level + "/10)");
		return appendTo;
	}
}
