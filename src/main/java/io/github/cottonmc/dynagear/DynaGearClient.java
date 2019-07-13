package io.github.cottonmc.dynagear;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.render.ColorProviderRegistryImpl;
import net.minecraft.item.ItemConvertible;

public class DynaGearClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		for (EquipmentSet set : MaterialConfig.EQUIPMENT.values()) {
			ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
				if (layer == 0) return set.getMaterial().getColor();
				else return -1;
			}, set.getAll().toArray(new ItemConvertible[]{}));
		}
	}
}
