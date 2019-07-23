package com.fuzs.consolehud.mixin.client.gui.hud;


import com.fuzs.consolehud.ConsoleHud;
import com.fuzs.consolehud.helper.TooltipHelper;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private int heldItemTooltipFade;

	@Shadow
	private ItemStack currentStack;

	@Shadow
	public abstract TextRenderer getFontRenderer();

	private TooltipHelper tooltipHelper = new TooltipHelper();
	private List<Text> tooltipCache = Lists.newArrayList();

	@Inject(
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;heldItemTooltipFade:I",
			ordinal = 0,
			shift = At.Shift.AFTER
		),
		method = "tick"
	)
	private void setHeldItemTooltipFade(CallbackInfo ci) {
		this.heldItemTooltipFade = ConsoleHud.CONFIG.heldItemTooltipsConfig.displayTime;
	}

	@Inject(
		at = @At(
			value = "HEAD"
		),
		method = "renderHeldItemTooltip",
		cancellable = true
	)
	private void drawCustomTooltip(CallbackInfo ci) {
		this.client.getProfiler().push("selectedItemName");
		if (ConsoleHud.CONFIG.heldItemTooltips && ConsoleHud.CONFIG.heldItemTooltipsConfig.rows > 0 && this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
			int posX = this.client.window.getScaledWidth() / 2;
			int posY = this.client.window.getScaledHeight();

			posX += ConsoleHud.CONFIG.heldItemTooltipsConfig.xOffset;
			posY -= ConsoleHud.CONFIG.heldItemTooltipsConfig.yOffset;

			if (!this.client.interactionManager.hasStatusBars()) {
				posY += 14;
			}

			/*if (ConsoleHud.CONFIG.GENERAL_CONFIG.hoveringHotbar.get() && ConsoleHud.CONFIG.heldItemTooltipsConfig.tied.get()) {
				posX += ConsoleHud.CONFIG.HOVERING_HOTBAR_CONFIG.xOffset.get();
				posY -= ConsoleHud.CONFIG.HOVERING_HOTBAR_CONFIG.yOffset.get();
			}*/

			int alpha = (int) Math.min(255.0F, (float) this.heldItemTooltipFade * 256.0F / 10.0F);

			if (alpha > 0) {

				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

				Identifier identifier = Registry.ITEM.getId(this.currentStack.getItem());
				boolean blacklisted = Arrays.asList(ConsoleHud.CONFIG.heldItemTooltipsConfig.blacklist).contains(identifier.toString()) || Arrays.asList(ConsoleHud.CONFIG.heldItemTooltipsConfig.blacklist).contains(identifier.getNamespace());

				// using -2 instead of -1 in case some lag interferes, will run twice most of the time then, but still better than 40 times
				if (!ConsoleHud.CONFIG.heldItemTooltipsConfig.cacheTooltip || this.heldItemTooltipFade > ConsoleHud.CONFIG.heldItemTooltipsConfig.displayTime - 2) {
					this.tooltipCache = this.tooltipHelper.createTooltip(this.currentStack, !ConsoleHud.CONFIG.heldItemTooltips || blacklisted || ConsoleHud.CONFIG.heldItemTooltipsConfig.rows == 1);
				}

				int size = this.tooltipCache.size();

				// clears the action bar so it won't overlap with the tooltip
				/*if (size > (ConsoleHud.CONFIG.hoveringHotbar ? 0 : 1)) {
					this.client.player.sendStatusMessage(new LiteralText(""), true);
				}*/

				posY -= size > 1 ? (size - 1) * 10 + 2 : (size - 1) * 10;

				for (int i = 0; i < size; i++) {
					String text = this.tooltipCache.get(i).asFormattedString();
					this.getFontRenderer().drawWithShadow(text, (float) (posX - this.getFontRenderer().getStringWidth(text) / 2), (float) posY, 16777215 + (alpha << 24));
					posY += i == 0 ? 12 : 10;

				}

				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			this.client.getProfiler().pop();
			ci.cancel();
		}
	}
}