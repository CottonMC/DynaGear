package io.github.cottonmc.dynagear.item;

import io.github.cottonmc.dynagear.ConfiguredMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DynaSwordItem extends SwordItem {
	private ConfiguredMaterial material;
	public DynaSwordItem(ConfiguredMaterial material, int baseAttackDamage, float attackSpeed, Settings settings) {
		super(material, baseAttackDamage, attackSpeed, settings);
		this.material = material;
	}

	@Override
	public String getTranslationKey() {
		return "item.dynagear.sword";
	}

	@Override
	public Text getName() {
		String mat = material.getMaterialName().substring(0, 1).toUpperCase() + material.getMaterialName().substring(1);
		return new TranslatableText(getTranslationKey(), mat);
	}

	@Override
	public Text getName(ItemStack stack) {
		String mat = material.getMaterialName().substring(0, 1).toUpperCase() + material.getMaterialName().substring(1);
		return new TranslatableText(getTranslationKey(), mat);
	}
}
