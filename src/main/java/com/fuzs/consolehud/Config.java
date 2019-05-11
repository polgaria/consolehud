package com.fuzs.consolehud;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import io.github.cottonmc.repackage.blue.endless.jankson.Comment;

@ConfigFile(name = ConsoleHud.MODID)
public class Config {
	@Comment("Enhances vanilla held item tooltips with information about enchantments, potions effects, shulker box contents, and more.")
	public boolean heldItemTooltips = true;

	@Comment("Shows a small player model in a configurable corner of the screen while the player is sprinting, sneaking, or flying.")
	public boolean paperDoll = true;

	@Comment("Disables held item tooltips for specified items and mods, mainly to prevent custom tooltips from overlapping.")
	public String[] heldItemTooltipsBlacklist = new String[]{""};

	@Comment("Maximum amount of rows to be displayed for held item tooltips.")
	public int heldItemTooltipsRows = 5;

	@Comment("Offset on x-axis from screen center.")
	public int heldItemTooltipsXOffset = 0;


	@Comment("Offset on y-axis from screen center.")
	public int heldItemTooltipsYOffset = 59;

	@Comment("Show three dots when the complete tooltip information can't be displayed like on Console Edition instead of the custom text.")
	public boolean heldItemTooltipsDots = false;

	@Comment("Defines a screen corner to display the paper doll in. [0: top left, 1: bottom left, 2: top right, 3: bottom right, default: 0]")
	public int paperDollPosition = 0;

	@Comment("Scale of the paper doll. This is additionally adjusted by the GUI Scale option in Video Settings.")
	public int paperDollScale = 4;

	@Comment("Offset on x-axis from original doll position.")
	public int paperDollXOffset = 0;

	@Comment("Offset on y-axis from original doll position.")
	public int paperDollYOffset = 0;

	@Comment("Always displays the paper doll, no matter what action the player is performing.")
	public boolean paperDollAlways = false;

	@Comment("Enables the paper doll while the player is sprinting.")
	public boolean paperDollSprinting = true;

	@Comment("Enables the paper doll while the player is crouching.")
	public boolean paperDollCrouching = true;

	@Comment("Displays the paper doll when the player is using creative mode flight.")
	public boolean paperDollFlying = true;

	@Comment("Shows the paper doll while the player is flying with an elytra.")
	public boolean paperDollElytraFlying = true;

	@Comment("Disables flame overlay on the hud when on fire and displays the burning paper doll instead.")
	public boolean paperDollBurning = false;

	@Comment("Shows the paper doll while the player is riding any entity.")
	public boolean paperDollRiding = false;
}