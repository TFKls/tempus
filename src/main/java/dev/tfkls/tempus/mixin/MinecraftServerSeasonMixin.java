package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.SeasonManager;
import dev.tfkls.tempus.core.SeasonServerState;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerSeasonMixin {
	@Inject(method = "loadWorld", at = @At(value = "TAIL"))
	void loadSeasonData(CallbackInfo ci) {
		SeasonManager.getInstance().loadServer((MinecraftServer) (Object) this);
	}

	@Inject(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z"))
	void saveSeasonData(CallbackInfo ci) {
		SeasonServerState.getServerState((MinecraftServer) (Object) this).loadSeasonManager(SeasonManager.getInstance());
	}
}
