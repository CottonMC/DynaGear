package io.github.cottonmc.dynagear.api;

public enum EquipmentCategory {
	/**
	 * Handheld tools. Gets passed two texture layers (layer0 is tinted), and a stick key for recipe patterns alongside the material key.
	 */
	TOOLS,
	/**
	 * Worn armor. Gets passed one tinted texture layer, and only the material key for recipe patterns.
	 */
	ARMOR
}
