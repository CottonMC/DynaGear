package io.github.cottonmc.dynagear;

import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import io.github.cottonmc.dynagear.api.EquipmentCategory;
import io.github.cottonmc.dynagear.api.EquipmentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourceBuilders {

	public static ShapedRecipeBuilder applyDict(ShapedRecipeBuilder builder, ConfiguredMaterial mat, EquipmentCategory category, EquipmentType type, boolean needsIngot) {
		if (category == EquipmentCategory.TOOLS) builder.ingredientItem('/', new Identifier("stick"));
		if (needsIngot) {
			if (mat.getMaterialId().indexOf('#') == 0) {
				Identifier id = new Identifier(mat.getMaterialId().substring(1));
				builder.ingredientTag('#', id);
			} else {
				Identifier id = new Identifier(mat.getMaterialId());
				builder.ingredientItem('#', id);
			}
		}
		if (type.getAdditionalIngredients(mat.getName()) != null) {
			Map<Character, String> ingredients = type.getAdditionalIngredients(mat.getName());
			for (Character ch : ingredients.keySet()) {
				String ing = ingredients.get(ch);
				if (ing.indexOf('#') == 0) {
					Identifier id = new Identifier(ing.substring(1));
					builder.ingredientTag(ch, id);
				} else {
					Identifier id = new Identifier(ing);
					builder.ingredientItem(ch, id);
				}
			}
		}
		return builder;
	}

	public static ShapedRecipeBuilder applyBlockDict(ShapedRecipeBuilder builder, ConfiguredMaterial mat, String blockMat, EquipmentCategory category, EquipmentType type, boolean needsIngot) {
		applyDict(builder, mat, category, type, needsIngot);
		if (blockMat.indexOf('#') == 0) {
			Identifier id = new Identifier(blockMat.substring(1));
			builder.ingredientTag('%', id);
		} else {
			Identifier id = new Identifier(blockMat);
			builder.ingredientItem('%', id);
		}
		return builder;
	}

	public static void createRecipes(EquipmentSet set) {
		ConfiguredMaterial material = set.getMaterial();
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			String piece = type.getSuffix();
			Identifier result = new Identifier(DynaGear.MODID, material.getName() + "_" + piece);
			String[] pattern = type.getCraftingPattern();
			final boolean[] needsIngot = new boolean[1];
			boolean needsBlock = false;
			for (String str : pattern) {
				if (str.contains("#")) needsIngot[0] = true;
				if (str.contains("%")) needsBlock = true;
			}
			if (needsBlock) {
				//don't register a recipe if the material has no block form!
				if (!material.getBlockMaterialId().equals("")) Registry.register(DynaGear.RECIPES, result, (builder) -> applyBlockDict(builder.pattern(type.getCraftingPattern()), material, material.getBlockMaterialId(), type.getCategory(), type, needsIngot[0]).result(result, 1));
			} else {
				Registry.register(DynaGear.RECIPES, result, (builder) -> applyDict(builder.pattern(type.getCraftingPattern()), material, type.getCategory(), type, needsIngot[0]).result(result, 1));
			}
		}
	}

	public static void appendTags(EquipmentSet set) {
		ConfiguredMaterial material = set.getMaterial();
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			String piece = type.getSuffix();
			Identifier tagId = type.getEquipmentTag();
			if (tagId == null) continue;
			Identifier result = new Identifier(DynaGear.MODID, material.getName() + "_" + piece);
			DynaGear.TAGS.putIfAbsent(tagId, new ArrayList<>());
			List<Identifier> toAdd = DynaGear.TAGS.get(tagId);
			toAdd.add(result);

		}
	}

	@Environment(EnvType.CLIENT)
	public static void createModels(EquipmentSet set) {
		ConfiguredMaterial material = set.getMaterial();
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			String piece = type.getSuffix();
			Identifier item = new Identifier(DynaGear.MODID, material.getName() + "_" + piece);
			EquipmentCategory category = type.getCategory();
			DynaGearClient.MODELS.put(item, (builder) -> applyModel(builder.parent(category == EquipmentCategory.ARMOR? new Identifier("item/generated") : new Identifier("item/handheld")), piece, category));
		}
	}

	public static ModelBuilder applyModel(ModelBuilder builder, String part, EquipmentCategory category) {
		if (category == EquipmentCategory.TOOLS) {
			builder.texture("layer0", new Identifier(DynaGear.MODID, "item/" + part + "_head"));
			builder.texture("layer1", new Identifier(DynaGear.MODID, "item/" + part + "_handle"));
		} else {
			builder.texture("layer0", new Identifier(DynaGear.MODID, "item/armor_" + part));
		}
		return builder;
	}
}
