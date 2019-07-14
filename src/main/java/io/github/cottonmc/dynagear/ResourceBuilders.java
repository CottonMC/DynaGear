package io.github.cottonmc.dynagear;

import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import io.github.cottonmc.dynagear.ConfiguredMaterial;
import io.github.cottonmc.dynagear.DynaGear;
import io.github.cottonmc.dynagear.DynaGearClient;
import io.github.cottonmc.dynagear.EquipmentSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class ResourceBuilders {
	public static final Map<String, Pair<String[], Boolean>> PATTERNS = new HashMap<>();

	public static ShapedRecipeBuilder applyDict(ShapedRecipeBuilder builder, String mat, boolean stick) {
		if (stick) builder.ingredientItem('/', new Identifier("stick"));
		if (mat.indexOf('#') == 0) {
			Identifier id = new Identifier(mat.substring(1));
			builder.ingredientTag('#', id);
		} else {
			Identifier id = new Identifier(mat);
			builder.ingredientItem('#', id);
		}
		return builder;
	}

	public static void createRecipes(EquipmentSet set) {
		ConfiguredMaterial material = set.getMaterial();
		for (String piece : PATTERNS.keySet()) {
			Identifier result = new Identifier(DynaGear.MODID, material.getMaterialName() + "_" + piece);
			Pair<String[], Boolean> pattern = PATTERNS.get(piece);
			DynaGear.RECIPES.put(result, (builder) -> applyDict(builder.pattern(pattern.getLeft()), material.getMaterialId(), pattern.getRight()).result(result, 1));
		}
	}

	@Environment(EnvType.CLIENT)
	public static void createModels(EquipmentSet set) {
		ConfiguredMaterial material = set.getMaterial();
		for (String piece : PATTERNS.keySet()) {
			Identifier item = new Identifier(DynaGear.MODID, material.getMaterialName() + "_" + piece);
			Boolean twoLayer = PATTERNS.get(piece).getRight();
			DynaGearClient.MODELS.put(item, (builder) -> applyModel(builder.parent(new Identifier("item/generated")), piece, twoLayer));
		}
	}

	public static ModelBuilder applyModel(ModelBuilder builder, String part, boolean tool) {
		if (tool) {
			builder.texture("layer0", new Identifier(DynaGear.MODID, "item/" + part + "_head"));
			builder.texture("layer1", new Identifier(DynaGear.MODID, "item/" + part + "_handle"));
		} else {
			builder.texture("layer0", new Identifier(DynaGear.MODID, "item/armor_" + part));
		}
		return builder;
	}

	static {
		PATTERNS.put("sword", new Pair<>(new String[]{"#", "#", "/"}, true));
		PATTERNS.put("shovel", new Pair<>(new String[]{"#", "/", "/"}, true));
		PATTERNS.put("pickaxe", new Pair<>(new String[]{"###", " / ", " / "}, true));
		PATTERNS.put("axe", new Pair<>(new String[]{"##", "#/", " /"}, true));
		PATTERNS.put("hoe", new Pair<>(new String[]{"##", " /", " /"}, true));
		PATTERNS.put("helmet", new Pair<>(new String[]{"###", "# #"}, false));
		PATTERNS.put("chestplate", new Pair<>(new String[]{"# #", "###", "###"}, false));
		PATTERNS.put("leggings", new Pair<>(new String[]{"###", "# #", "# #"}, false));
		PATTERNS.put("boots", new Pair<>(new String[]{"# #", "# #"}, false));
	}
}
