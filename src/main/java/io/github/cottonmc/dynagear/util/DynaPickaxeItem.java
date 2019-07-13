package io.github.cottonmc.dynagear.util;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;

public class DynaPickaxeItem extends PickaxeItem {
	public DynaPickaxeItem(ToolMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
		super(material, baseAttackDamage, attackSpeed, settings);
	}
}
