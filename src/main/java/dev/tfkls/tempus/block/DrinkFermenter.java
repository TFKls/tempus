package dev.tfkls.tempus.block;

import dev.tfkls.tempus.block.entity.DrinkFermenterEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DrinkFermenter extends AbstractFurnaceBlock {

	public DrinkFermenter(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DrinkFermenterEntity(pos, state);
	}

	@Override
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof DrinkFermenterEntity) {
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
		}
	}
}
