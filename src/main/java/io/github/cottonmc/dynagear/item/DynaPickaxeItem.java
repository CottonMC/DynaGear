package io.github.cottonmc.dynagear.item;

import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DynaPickaxeItem extends PickaxeItem {
	private ConfiguredMaterial material;
	public DynaPickaxeItem(ConfiguredMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
		super(material.asTool(), baseAttackDamage, attackSpeed, settings);
		this.material = material;
	}

	@Override
	public String getTranslationKey() {
		return "item.dynagear.pickaxe";
	}

	@Override
	public Text getName() {
		String mat = material.getName().substring(0, 1).toUpperCase() + material.getName().substring(1);
		return new TranslatableText(getTranslationKey(), mat);
	}

	@Override
	public Text getName(ItemStack stack) {
		String mat = material.getName().substring(0, 1).toUpperCase() + material.getName().substring(1);
		return new TranslatableText(getTranslationKey(), mat);
	}
}
