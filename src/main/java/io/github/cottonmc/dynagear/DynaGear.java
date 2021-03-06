package io.github.cottonmc.dynagear;

import com.mojang.serialization.Lifecycle;
import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import com.swordglowsblue.artifice.common.ArtificeRegistry;
import io.github.cottonmc.dynagear.api.*;
import io.github.cottonmc.dynagear.impl.EquipmentManager;
import io.github.cottonmc.dynagear.impl.MaterialManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class DynaGear implements ModInitializer {
	public static final String MODID = "dynagear";

	public static final Logger logger = LogManager.getLogger();

	public static final ItemGroup DYNAGEAR_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "dynagear"), () -> new ItemStack(Items.DIAMOND_CHESTPLATE));
	public static final RegistryKey<Registry<Processor<ShapedRecipeBuilder>>> RECIPES_KEY = RegistryKey.ofRegistry(new Identifier(MODID,"dynagear_recipes"));
	public static final Registry<Processor<ShapedRecipeBuilder>> RECIPES = new SimpleRegistry<>(RECIPES_KEY, Lifecycle.stable());

	public static final Map<Identifier, List<Identifier>> TAGS = new HashMap<>();
	public static final Map<Identifier, ConfiguredMaterial> MATERIALS = new HashMap<>();
	public static final List<EquipmentType> EQUIPMENT_TYPES = new ArrayList<>();
	public static final RegistryKey<Registry<EquipmentSet>> EQUIPMENT_KEY = RegistryKey.ofRegistry(new Identifier(MODID,"dynagear_equipment_sets"));
	public static final Registry<EquipmentSet> EQUIPMENT = new SimpleRegistry<>(EQUIPMENT_KEY,Lifecycle.stable());

	@Override
	public void onInitialize() {
		MaterialConfig.loadConfig();
		MaterialManager mats = new MaterialManager();
		EquipmentManager equipment = new EquipmentManager();
		FabricLoader.getInstance().getEntrypoints("dynagear:materials", MaterialAdder.class).forEach(adder -> adder.addMaterials(mats));
		FabricLoader.getInstance().getEntrypoints("dynagear:equipment_types", EquipmentTypeAdder.class).forEach(adder -> adder.addEquipmentTypes(equipment));
		EQUIPMENT_TYPES.addAll(equipment.getTypes());

		for (ConfiguredMaterial mat : mats.getMaterials()) {
			Identifier id = new Identifier(MODID, mat.getName());
			MATERIALS.putIfAbsent(id, mat);
		}
		List<Identifier> matIds = new ArrayList<>(MATERIALS.keySet());
		matIds.sort(Comparator.comparing(Identifier::getPath));
		for (Identifier id : matIds) {
			ConfiguredMaterial mat = MATERIALS.get(id);
			Registry.register(EQUIPMENT, new Identifier(DynaGear.MODID, mat.getName()), EquipmentSet.create(mat));
		}

		Artifice.registerData(new Identifier(MODID, "dynagear_data"), data -> {
			for (Identifier id : RECIPES.getIds()) {
				data.addShapedRecipe(id, (builder) -> RECIPES.get(id).process(builder));
			}
			for (Identifier id : TAGS.keySet()) {
				data.addItemTag(id, (builder) -> builder.values(TAGS.get(id).toArray(new Identifier[0])));
			}
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String path = FabricLoader.getInstance().getGameDirectory().toPath().resolve("dynagear_export").toString();
			try {
				((ArtificeResourcePack)ArtificeRegistry.DATA.get(new Identifier(MODID, "dynagear_data"))).dumpResources(path);
			} catch (IOException e) {
				logger.warn("[DynaGear] Couldn't dump data packs!");
			}
		}
	}

	public static Item.Settings getSettings() {
		return new Item.Settings().group(DynaGear.DYNAGEAR_GROUP);
	}

}
