package io.github.cottonmc.dynagear.api;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Map;

public interface EquipmentType {
	/**
	 * @return The suffix to append onto material names for the item ID.
	 */
	String getSuffix();

	/**
	 * @return The crafting pattern for this equipment type.
	 * Use `#` for materials, `%` for material blocks, and `/` for sticks.
	 * Add more ingredients with {@link EquipmentType#getAdditionalIngredients()}
	 */
	String[] getCraftingPattern();

	/**
	 * @return Key-value pairs of additional ingredients required for this type's recipe.
	 * Should be independent of material.
	 * `#`, `%`, and `/` are reserved already.
	 */
	Map<Character, String> getAdditionalIngredients();

	/**
	 * @return The item tag to put this equipment type into, if any.
	 */
	@Nullable
	Identifier getEquipmentTag();

	/**
	 * @return The equipment category that this equipment is part of, either tools or armor.
	 */
	EquipmentCategory getCategory();

	/**
	 * Construct an item of this equipment type.
	 * @param material The material to get properties from.
	 * @return The constructed item.
	 */
	Item construct(ConfiguredMaterial material);
}
