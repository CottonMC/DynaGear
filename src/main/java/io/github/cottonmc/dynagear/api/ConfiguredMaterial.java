package io.github.cottonmc.dynagear.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ConfiguredMaterial {
	protected static final int[] BASE_ARMOR_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final String color;
	private Integer colorVal;
	private final String matId;
	private Ingredient ingredient;
	private final String blockMatId;
	private final int enchantability;
	private final int toolDurability;
	private final int miningLevel;
	private final float miningSpeed;
	private final float attackDamage;
	private final int armorDurabilityMultiplier;
	private final int[] protectionAmounts;
	private final SoundEvent equipSound;
	private final float toughness;

	/**
	 * The parameters for creating an equipment set.
	 * @param name The name of the material.
	 * @param color The color of the material, in #AARRGGBB or int ARGB form.
	 * @param matId The ID of the crafting/repair material. (prefix with # for a tag)
	 * @param blockMatId The ID of the block form of the crafting/repair material. (prefix with # for a tag) Set to "" for empty.
	 * @param enchantability How enchantable the material is, from 0 to 25.
	 * @param toolDurability How much durability tools of this material have.
	 * @param miningLevel The max level of block this tool can mine.
	 * @param miningSpeed The mining speed of this tool. (bigger is better)
	 * @param attackDamage How much base damage equipment of this material should do.
	 * @param armorDurabilityMultiplier How much armor of this material should have its durability multiplied by. (see {@link ConfiguredMaterial#BASE_ARMOR_DURABILITY})
	 * @param protectionAmounts How many armor points each piece of armor gives, in order from foot to head.
	 * @param toughness How much armor toughness this material's armor should give.
	 * @param equipSound The sound to play when equipping this material's armor.
	 */
	public ConfiguredMaterial(String name, String color, String matId, String blockMatId, int enchantability,
							  int toolDurability, int miningLevel, float miningSpeed, float attackDamage,
							  int armorDurabilityMultiplier, int[] protectionAmounts, float toughness, SoundEvent equipSound) {
		this.name = name;
		this.color = color;
		this.matId = matId;
		this.blockMatId = blockMatId;
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

	public String getName() {
		return name;
	}

	public int getColor() {
		if (colorVal != null) {
			return colorVal;
		}
		return colorVal = Integer.decode(color.replace("#", "0x")) | 0xFF000000;
	}

	public String getMaterialId() {
		return matId;
	}

	public Ingredient getIngredient() {
		if (ingredient != null) return ingredient;
		if (matId.indexOf('#') == 0) {
			Identifier id = new Identifier(matId.substring(1));
			return ingredient = Ingredient.fromTag(ItemTags.getContainer().get(id));
		} else {
			Identifier id = new Identifier(matId);
			return ingredient = Ingredient.ofItems(Registry.ITEM.get(id));
		}
	}

	public String getBlockMaterialId() {
		return blockMatId;
	}

	/**
	 * @return This material, passable to a ToolItem.
	 */
	public ConfiguredTool asTool() {
		return new ConfiguredTool();
	}

	/**
	 * @return This material, passable to an ArmorItem.
	 */
	public ConfiguredArmor asArmor() {
		return new ConfiguredArmor();
	}

	public class ConfiguredTool implements ToolMaterial {

		@Override
		public int getDurability() {
			return toolDurability;
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
		public int getMiningLevel() {
			return miningLevel;
		}

		@Override
		public int getEnchantability() {
			return enchantability;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return getIngredient();
		}
	}

	public class ConfiguredArmor implements ArmorMaterial {

		@Override
		public int getDurability(EquipmentSlot slot) {
			return armorDurabilityMultiplier * BASE_ARMOR_DURABILITY[slot.getEntitySlotId()];
		}

		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return protectionAmounts[slot.getEntitySlotId()];
		}

		@Override
		public int getEnchantability() {
			return enchantability;
		}

		@Override
		public SoundEvent getEquipSound() {
			return equipSound;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return getIngredient();
		}

		@Override
		@Environment(EnvType.CLIENT)
		//Used for getting the armor texture! Override this in a subclass if you want to change the texture your mat uses.
		public String getName() {
			return "dyna";
		}

		@Override
		public float getToughness() {
			return toughness;
		}
	}
}
