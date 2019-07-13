package io.github.cottonmc.dynagear;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import io.github.cottonmc.dynagear.util.ResourceBuilders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.render.ColorProviderRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynaGearClient implements ClientModInitializer {

	//TODO: figure out a sane way to get translations for materials
	public static final Map<String, Pair<String, String>> ITEM_TRANSLATIONS = new HashMap<>();
	public static final Map<Identifier, Processor<ModelBuilder>> MODELS = new HashMap<>();

	@Override
	public void onInitializeClient() {
		for (EquipmentSet set : MaterialConfig.EQUIPMENT.values()) {
			ResourceBuilders.createModels(set);
			ResourceBuilders.createTranslations(set);
		}
		Artifice.registerAssets(new Identifier(DynaGear.MODID, "dynagear_assets"), assets -> {
			for (Identifier id : MODELS.keySet()) {
				assets.addItemModel(id, MODELS.get(id));
			}
			//TODO: figure out a sane way to generate translations for other locales
			assets.addTranslations(new Identifier(DynaGear.MODID, "en_us"), lang -> {
				for (String key : ITEM_TRANSLATIONS.keySet()) {
					Pair<String, String> tl = ITEM_TRANSLATIONS.get(key);
					String value = I18n.translate(tl.getLeft(), tl.getRight());
					lang.entry(key, value);
				}
			});
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String path = FabricLoader.getInstance().getGameDirectory().toPath().resolve("dynagear_export").toString();
			try {
				Artifice.ASSETS.get(new Identifier(DynaGear.MODID, "dynagear_assets")).dumpResources(path);
			} catch (IOException e) {
				DynaGear.logger.warn("Couldn't dump resource packs!!");
			}
		}
		for (EquipmentSet set : MaterialConfig.EQUIPMENT.values()) {
			ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
				if (layer == 0) return set.getMaterial().getColor();
				else return -1;
			}, set.getAll().toArray(new ItemConvertible[]{}));
		}
	}
}
