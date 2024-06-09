package dev.tfkls.tempus.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import dev.tfkls.tempus.effects.CustomStatusEffects;
import dev.tfkls.tempus.manager.ThirstManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudThirstMixin {
	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow
	protected abstract int getHeartRows(int heartCount);

	@Shadow private int ticks;
	// TODO: Change icon provider to water drops (add custom textures)
	@Unique
	private static final int emptyDropIcon = 0;
	@Unique
	private static final int fullDropIcon = 9;
	@Unique
	private static final int halfDropIcon = 18;

	@Inject(method = "renderStatusBars", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;min(II)I"))
	void renderThirstBar(DrawContext context, CallbackInfo ci, @Local(name = "t") LocalIntRef renderHeightRef, @Local(name = "n") int renderWidth, @Local(name = "x") int mountHeartCount) {
		MinecraftClient client = ((InGameHudAccessor) this).getClient();
		Random random = ((InGameHudAccessor) this).getRandom();
		Identifier icons = new Identifier("tempus", "icons.png");
		PlayerEntity player = this.getCameraPlayer();
		int renderHeight = renderHeightRef.get() - (this.getHeartRows(mountHeartCount) - 1) * 10; // This is copied from the "air" rendering

		client.getProfiler().swap("thirst");
		if (player.isSubmergedIn(FluidTags.WATER) || player.getAir() < player.getMaxAir())
			return; // This ensures we don't clash with air indicators

		int currentThirst = ((ThirstManager.MixinAccessor) player).tempus$getThirstManager().getThirst();
		int offset = 0;
		if (player.hasStatusEffect(CustomStatusEffects.THIRST)) offset = 27;

		for (int loc = 0; loc < 10; loc++) {
			int verticalPosition = renderHeight;
			if (currentThirst <= 10 && this.ticks % (3*currentThirst+1) == 0) {
				verticalPosition += random.nextInt(3) - 1;
			}
			int horizontalPosition = renderWidth - 8 * loc - 9;
			context.drawTexture(icons, horizontalPosition, verticalPosition, emptyDropIcon+offset, 0, 9, 9);
			if (loc * 2 + 1 < currentThirst) {
				context.drawTexture(icons, horizontalPosition, verticalPosition, fullDropIcon+offset, 0, 9, 9);
			}
			if (loc * 2 + 1 == currentThirst) {
				context.drawTexture(icons, horizontalPosition, verticalPosition, halfDropIcon+offset, 0, 9, 9);
			}
		}
		renderHeightRef.set(renderHeight);
	}
}
