package io.github.cottonmc.dynagear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

import java.awt.*;
import java.util.function.Supplier;

public class ConfiguredMaterial implements ArmorMaterial, ToolMaterial {
	private static final int[] BASE_ARMOR_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final String color;
	private Integer colorVal;
	private final String matId;
	private Ingredient ingredient;
	private final int enchantability;
	private final int toolDurability;
	private final int miningLevel;
	private final float miningSpeed;
	private final float attackDamage;
	private final int armorDurabilityMultiplier;
	private final int[] protectionAmounts;
	private final SoundEvent equipSound;
	private final float toughness;

	public ConfiguredMaterial(String name, String color, String matId, int enchantability,
							  int toolDurability, int miningLevel, float miningSpeed, float attackDamage,
							  int armorDurabilityMultiplier, int[] protectionAmounts, float toughness, SoundEvent equipSound) {
		this.name = name;
		this.color = color;
		this.matId = matId;
		this.enchantability = enchantability;
		this.toolDurability = toolDurability;
		this.miningLevel = miningLevel;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.armorDurabilityMultiplier = armorDurabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.toughness = toughness;
		this.equipSound = equipSound;
	}

	public String getMaterialName() {
		return name;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getName() {
		return getMaterialName();
	}

	public int getColor() {
		if (colorVal != null) {
			return colorVal;
		}
		if (color.startsWith("#")) {
			int a = Integer.valueOf(color.substring(1, 3), 16);
			int r = Integer.valueOf(color.substring(3, 5), 16);
			int g = Integer.valueOf(color.substring(5, 7), 16);
			int b = Integer.valueOf(color.substring(7, 9), 16);
			return colorVal = new Color(r, g, b, a).getRGB();
		}
		return colorVal = Color.decode(color).getRGB();
	}

	public String getMaterialId() {
		return matId;
	}

	@Override
	public Ingredient getRepairIngredient() {
		if (ingredient != null) return ingredient;
		if (matId.indexOf('#') == 0) {
			Identifier id = new Identifier(matId.substring(1));
			return ingredient = Ingredient.fromTag(ItemTags.getContainer().get(id));
		} else {
			Identifier id = new Identifier(matId);
			return ingredient = Ingredient.ofItems(Registry.ITEM.get(id));
		}
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public int getDurability() {
		return toolDurability;
	}

	@Override
	public int getMiningLevel() {
		return miningLevel;
	}

	@Override
	public float getMiningSpeed() {
		return miningSpeed;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getDurability(EquipmentSlot slot) {
		return armorDurabilityMultiplier * BASE_ARMOR_DURABILITY[slot.getEntitySlotId()];
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}
}
