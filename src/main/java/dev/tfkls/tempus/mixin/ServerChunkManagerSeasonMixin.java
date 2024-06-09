package dev.tfkls.tempus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import dev.tfkls.tempus.manager.SeasonManager;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerSeasonMixin {

	// This method injects on ChunkTicketManager.getTickedChunkedCount(), as it requires the variable `i` to be initialized.
	// As such, injection on GameRules.getInt() does not work, as even with INVOKE_ASSIGN it is not yet accessible to us.
	@Inject(method = "tickChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ChunkTicketManager;getTickedChunkCount()I"))
	public void updateCropGrowth(CallbackInfo ci, @Local(name = "i") LocalIntRef randomTickSpeed) {
		randomTickSpeed.set(SeasonManager.getInstance().modifyTickSpeed(randomTickSpeed.get()));
	}
}
