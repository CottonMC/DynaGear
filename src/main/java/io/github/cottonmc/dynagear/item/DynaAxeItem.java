package io.github.cottonmc.dynagear.item;

import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DynaAxeItem extends AxeItem {
	private ConfiguredMaterial material;
	public DynaAxeItem(ConfiguredMaterial material, float baseAttackDamage, float attackSpeed, Settings settings) {
		super(material.asTool(), baseAttackDamage, attackSpeed, settings);
		this.material = material;
	}

	@Override
	public String getTranslationKey() {
		return "item.dynagear.axe";
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
