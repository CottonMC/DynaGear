package io.github.cottonmc.dynagear.api;

import io.github.cottonmc.dynagear.impl.MaterialManager;

/**
 * Use this interface via the `dynagear:materials` entrypoint.
 */
public interface MaterialAdder {
	/**
	 * Called to register new equipment materials.
	 * @param manager The manager for materials. Will reject registrations with duplicate names. Defers to config.
	 */
	void addMaterials(MaterialManager manager);
}
