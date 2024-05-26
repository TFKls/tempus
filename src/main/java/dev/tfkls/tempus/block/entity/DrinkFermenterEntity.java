package dev.tfkls.tempus.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static dev.tfkls.tempus.init.DrinkFermenterInitializer.*;

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
