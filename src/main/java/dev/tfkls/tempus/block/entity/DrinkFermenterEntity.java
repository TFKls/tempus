package dev.tfkls.tempus.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static dev.tfkls.tempus.init.DrinkFermenterInitializer.BLOCK_ENTITY_TYPE;
import static dev.tfkls.tempus.init.DrinkFermenterInitializer.RECIPE_TYPE;

public class DrinkFermenterEntity extends AbstractFurnaceBlockEntity {
	public DrinkFermenterEntity(BlockPos pos, BlockState state) {
		super(BLOCK_ENTITY_TYPE, pos, state, RECIPE_TYPE);
	}

	@Override
	protected Text getContainerName() {
		return Text.of("Fermenter");
	}

	// TODO: replace with custom screen handler
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new FurnaceScreenHandler(syncId, playerInventory);
	}
}
