package io.github.cottonmc.dynagear;


import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DynaGear implements ModInitializer {
	public static final String MODID = "dynagear";

	public static final Logger logger = LogManager.getLogger();

	public static final ItemGroup DYNAGEAR_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "dynagear"), () -> new ItemStack(Items.DIAMOND_CHESTPLATE));

	public static final Map<Identifier, Processor<ShapedRecipeBuilder>> RECIPES = new HashMap<>();

	@Override
	public void onInitialize() {
		MaterialConfig.loadConfig();
		Artifice.registerData(new Identifier(MODID, "dynagear_data"), data -> {
			for (Identifier id : RECIPES.keySet()) {
				data.addShapedRecipe(id, RECIPES.get(id));
			}
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String path = FabricLoader.getInstance().getGameDirectory().toPath().resolve("dynagear_export").toString();
			try {
				Artifice.DATA.get(new Identifier(MODID, "dynagear_data")).dumpResources(path);
			} catch (IOException e) {
				logger.warn("Couldn't dump data packs!!");
			}
		}
	}
}
