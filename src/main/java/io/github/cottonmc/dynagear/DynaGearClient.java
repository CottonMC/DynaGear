package io.github.cottonmc.dynagear;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.render.ColorProviderRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class DynaGearClient implements ClientModInitializer {

	//TODO: figure out a sane way to get translations for material types
	public static final Map<Identifier, Processor<ModelBuilder>> MODELS = new HashMap<>();

	@Override
	public void onInitializeClient() {
		for (Identifier id : DynaGear.EQUIPMENT.getIds()) {
			ResourceBuilders.createModels(DynaGear.EQUIPMENT.get(id));
		}
		Artifice.registerAssets(new Identifier(DynaGear.MODID, "dynagear_assets"), assets -> {
			for (Identifier id : MODELS.keySet()) {
				assets.addItemModel(id, MODELS.get(id));
			}
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String path = FabricLoader.getInstance().getGameDirectory().toPath().resolve("dynagear_export").toString();
			try {
				Artifice.ASSETS.get(new Identifier(DynaGear.MODID, "dynagear_assets")).dumpResources(path);
			} catch (IOException e) {
				DynaGear.logger.warn("Couldn't dump resource packs!!");
			}
		}
		for (Identifier id : DynaGear.EQUIPMENT.getIds()) {
			EquipmentSet set = DynaGear.EQUIPMENT.get(id);
			ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
				if (layer == 0) return set.getMaterial().getColor();
				else return -1;
			}, set.getAll().toArray(new ItemConvertible[]{}));
		}
	}
}
