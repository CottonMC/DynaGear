package io.github.cottonmc.dynagear.impl;

import io.github.cottonmc.dynagear.DynaGear;
import io.github.cottonmc.dynagear.api.EquipmentCategory;
import io.github.cottonmc.dynagear.api.EquipmentType;
import io.github.cottonmc.dynagear.item.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

import java.util.*;

public class EquipmentManager {
	private final Map<String, EquipmentType> TYPES = new HashMap<>();
	private final List<EquipmentType> BUILTIN_TYPES = new ArrayList<>();

	public EquipmentManager() {
		//always display vanilla equipment types first and in the vanilla order!
		addBuiltinEquipment(new SimpleEquipmentType("sword", new String[]{"#", "#", "/"}, new Identifier("fabric:swords"), EquipmentCategory.TOOLS,
				material -> new DynaSwordItem(material, 3, -2.4f, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("shovel", new String[]{"#", "/", "/"}, new Identifier("fabric:shovels"), EquipmentCategory.TOOLS,
				material -> new DynaShovelItem(material, 1.5F, -3.0F, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("pickaxe", new String[]{"###", " / ", " / "}, new Identifier("fabric:pickaxes"), EquipmentCategory.TOOLS,
				material -> new DynaPickaxeItem(material, 1, -2.8F, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("axe", new String[]{"##", "#/", " /"}, new Identifier("fabric:axes"), EquipmentCategory.TOOLS,
				material -> new DynaAxeItem(material, 6.0F, -3.2F, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("hoe", new String[]{"##", " /", " /"}, new Identifier("fabric:hoes"), EquipmentCategory.TOOLS,
				material -> new DynaHoeItem(material, material.asTool().getMiningLevel()-3, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("helmet", new String[]{"###", "# #"}, null, EquipmentCategory.ARMOR,
				material -> new DynaArmorItem(material, "helmet", EquipmentSlot.HEAD, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("chestplate", new String[]{"# #", "###", "###"}, null, EquipmentCategory.ARMOR,
				material -> new DynaArmorItem(material, "chestplate", EquipmentSlot.CHEST, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("leggings", new String[]{"###", "# #", "# #"}, null, EquipmentCategory.ARMOR,
				material -> new DynaArmorItem(material, "leggings", EquipmentSlot.LEGS, DynaGear.getSettings())));
		addBuiltinEquipment(new SimpleEquipmentType("boots", new String[]{"# #", "# #"}, null, EquipmentCategory.ARMOR,
				material -> new DynaArmorItem(material, "boots", EquipmentSlot.FEET, DynaGear.getSettings())));
	}

	private void addBuiltinEquipment(EquipmentType type) {
		BUILTIN_TYPES.add(type);
		addEquipmentType(type);
	}

	public void addEquipmentType(EquipmentType type) {
		TYPES.putIfAbsent(type.getSuffix(), type);
	}

	public List<EquipmentType> getTypes() {
		for (EquipmentType builtin : BUILTIN_TYPES) {
			TYPES.remove(builtin.getSuffix());
		}
		List<EquipmentType> types = new ArrayList<>(TYPES.values());
		types.sort(Comparator.comparing(EquipmentType::getSuffix));
		List<EquipmentType> ret = new ArrayList<>(BUILTIN_TYPES);
		ret.addAll(types);
		return ret;
	}
}
