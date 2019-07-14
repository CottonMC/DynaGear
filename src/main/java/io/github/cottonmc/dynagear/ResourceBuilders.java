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

public class ResourceBuilders {

	public static ShapedRecipeBuilder applyDict(ShapedRecipeBuilder builder, String mat, EquipmentCategory category) {
		if (category == EquipmentCategory.TOOLS) builder.ingredientItem('/', new Identifier("stick"));
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
		for (EquipmentType type : DynaGear.EQUIPMENT_TYPES) {
			String piece = type.getSuffix();
			Identifier result = new Identifier(DynaGear.MODID, material.getName() + "_" + piece);
			Registry.register(DynaGear.RECIPES, result, (builder) -> applyDict(builder.pattern(type.getCraftingPattern()), material.getMaterialId(), type.getCategory()).result(result, 1));
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
			DynaGearClient.MODELS.put(item, (builder) -> applyModel(builder.parent(new Identifier("item/generated")), piece, category));
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
