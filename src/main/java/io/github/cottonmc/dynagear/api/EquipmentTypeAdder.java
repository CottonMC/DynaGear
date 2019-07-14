package io.github.cottonmc.dynagear.api;

import io.github.cottonmc.dynagear.impl.EquipmentManager;

/**
 * Use this interface via the `dynagear:equipment_types` entrypoint.
 */
public interface EquipmentTypeAdder {
	/**
	 * Called to register new equipment types.
	 * @param manager The manager for equipment types. Will reject registrations with duplicate names.
	 */
	void addEquipmentTypes(EquipmentManager manager);
}
