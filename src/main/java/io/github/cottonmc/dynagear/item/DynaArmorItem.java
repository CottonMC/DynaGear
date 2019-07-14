package io.github.cottonmc.dynagear.item;

import io.github.cottonmc.dynagear.ConfiguredMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DynaArmorItem extends ArmorItem {
	private ConfiguredMaterial material;
	private String type;
	public DynaArmorItem(ConfiguredMaterial material, String type, EquipmentSlot slot, Settings settings) {
		super(material.asArmor(), slot, settings);
		this.material = material;
		this.type = type;
	}

	@Override
	public String getTranslationKey() {
		return "item.dynagear." + type;
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
