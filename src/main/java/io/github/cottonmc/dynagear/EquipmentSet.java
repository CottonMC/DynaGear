package io.github.cottonmc.dynagear;


import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import io.github.cottonmc.dynagear.api.EquipmentType;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EquipmentSet {
	private String name;
	private ConfiguredMaterial material;
	private Map<String, Item> equipment = new HashMap<>();


	private EquipmentSet(ConfiguredMaterial material) {
		this.name = material.getName();
		this.material = material;
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			equipment.put(name + "_"+type.getSuffix(), type.construct(material));
		}
	}

	private void registerAll() {
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			String key = name + "_" + type.getSuffix();
			Registry.register(Registry.ITEM, new Identifier(DynaGear.MODID, key), equipment.get(key));
		}
	}

	public static EquipmentSet create(ConfiguredMaterial material) {
		EquipmentSet set = new EquipmentSet(material);
		set.registerAll();
		ResourceBuilders.createRecipes(set);
		ResourceBuilders.appendTags(set);
		return set;
	}

	public ConfiguredMaterial getMaterial() {
		return material;
	}

	public Item get(String type) {
		return equipment.get(name + "_"+type);
	}

	public Collection<Item> getAll() {
		return equipment.values();
	}

}
