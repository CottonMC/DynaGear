package io.github.cottonmc.dynagear.impl;

import io.github.cottonmc.dynagear.api.ConfiguredMaterial;

import java.util.*;

public class MaterialManager {
	private Map<String, ConfiguredMaterial> MATERIALS = new HashMap<>();

	public void registerMaterial(ConfiguredMaterial material) {
		MATERIALS.put(material.getName(), material);
	}

	public List<ConfiguredMaterial> getMaterials() {
		List<ConfiguredMaterial> materials = new ArrayList<>(MATERIALS.values());
		materials.sort(Comparator.comparing(ConfiguredMaterial::getName));
		return materials;
	}
}
