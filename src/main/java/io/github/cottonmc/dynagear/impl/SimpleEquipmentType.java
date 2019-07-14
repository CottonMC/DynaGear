package io.github.cottonmc.dynagear.impl;

import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import io.github.cottonmc.dynagear.api.EquipmentCategory;
import io.github.cottonmc.dynagear.api.EquipmentType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleEquipmentType implements EquipmentType {
	private String suffix;
	private String[] pattern;
	@Nullable
	private Identifier tag;
	private EquipmentCategory category;
	private Function<ConfiguredMaterial, Item> constructor;

	public SimpleEquipmentType(String suffix, String[] pattern, Identifier tag, EquipmentCategory category, Function<ConfiguredMaterial, Item> constructor) {
		this.suffix = suffix;
		this.pattern = pattern;
		this.tag = tag;
		this.category = category;
		this.constructor = constructor;
	}

	@Override
	public String getSuffix() {
		return suffix;
	}

	@Override
	public String[] getCraftingPattern() {
		return pattern;
	}

	@Nullable
	@Override
	public Identifier getEquipmentTag() {
		return tag;
	}

	@Override
	public EquipmentCategory getCategory() {
		return category;
	}

	@Override
	public Item construct(ConfiguredMaterial material) {
		return constructor.apply(material);
	}
}
