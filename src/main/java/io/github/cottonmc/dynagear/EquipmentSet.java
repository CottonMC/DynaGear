package io.github.cottonmc.dynagear;


import io.github.cottonmc.dynagear.util.DynaAxeItem;
import io.github.cottonmc.dynagear.util.DynaPickaxeItem;
import io.github.cottonmc.dynagear.util.ResourceBuilders;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EquipmentSet {
	private String name;
	private ConfiguredMaterial material;
	private Map<String, Item> equipment = new HashMap<>();
	private static Item.Settings getSettings() {
		return new Item.Settings().group(DynaGear.DYNAGEAR_GROUP);
	}

	private EquipmentSet(ConfiguredMaterial material) {
		this.name = material.getName();
		this.material = material;
		equipment.put(name+"_sword", new SwordItem(material, 3, -2.4f, getSettings()));
		equipment.put(name+"_shovel", new ShovelItem(material, 1.5F, -3.0F, getSettings()));
		equipment.put(name+"_pickaxe", new DynaPickaxeItem(material, 1, -2.8F, getSettings()));
		equipment.put(name+"_axe", new DynaAxeItem(material, 6.0F, -3.2F, getSettings()));
		equipment.put(name+"_hoe", new HoeItem(material, material.getMiningLevel()-3, getSettings()));
		equipment.put(name+"_helmet", new ArmorItem(material, EquipmentSlot.HEAD, getSettings()));
		equipment.put(name+"_chestplate", new ArmorItem(material, EquipmentSlot.CHEST, getSettings()));
		equipment.put(name+"_leggings", new ArmorItem(material, EquipmentSlot.LEGS, getSettings()));
		equipment.put(name+"_boots", new ArmorItem(material, EquipmentSlot.FEET, getSettings()));
	}

	private void registerAll() {
		for (String key : equipment.keySet()) {
			Registry.register(Registry.ITEM, new Identifier(DynaGear.MODID, key), equipment.get(key));
		}
	}

	public static EquipmentSet create(ConfiguredMaterial material) {
		EquipmentSet set = new EquipmentSet(material);
		set.registerAll();
		ResourceBuilders.createRecipes(set);
		return set;
	}

	public ConfiguredMaterial getMaterial() {
		return material;
	}

	public Item getSword() {
		return equipment.get(name+"_sword");
	}

	public Item getShovel() {
		return equipment.get(name+"_shovel");
	}

	public Item getPickaxe() {
		return equipment.get(name+"_pickaxe");
	}

	public Item getAxe() {
		return equipment.get(name+"_axe");
	}

	public Item getHoe() {
		return equipment.get(name+"_hoe");
	}

	public Item getHelmet() {
		return equipment.get(name+"_helmet");
	}

	public Item getChestplate() {
		return equipment.get(name+"_chestplate");
	}

	public Item getLeggings() {
		return equipment.get(name+"_leggings");
	}

	public Item getBoots() {
		return equipment.get(name+"_boots");
	}

	public Item get(String type) {
		return equipment.get(name+"_"+type);
	}

}