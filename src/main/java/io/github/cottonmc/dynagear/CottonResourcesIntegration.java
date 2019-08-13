package io.github.cottonmc.dynagear;

import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import io.github.cottonmc.dynagear.api.MaterialAdder;
import io.github.cottonmc.dynagear.impl.MaterialManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;

public class CottonResourcesIntegration implements MaterialAdder {
	private static Map<String, ConfiguredMaterial> materials = new HashMap<>();
	
	
	@Override
	public void addMaterials(MaterialManager manager) {
		if (FabricLoader.getInstance().isModLoaded("cotton-resources")) {
			//TODO: switch to dynamic once ore vote is in?
			for (ConfiguredMaterial mat : materials.values()) {
				manager.registerMaterial(mat);
			}
		}
	}
	
	static {
		materials.put("aluminum", new ConfiguredMaterial("aluminum", "#fff9b9b9", "#c:aluminum_ingot", "#c:aluminum_block", 16,
				300, 1, 5, 2,
				16, new int[]{3, 4, 5, 3}, 0.25f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("brass", new ConfiguredMaterial("brass", "#ffffbe3a", "#c:brass_ingot", "#c:brass_block", 16,
				400, 2, 5.5f, 2.5f,
				18, new int[]{4, 4, 4, 4}, 0.3f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("cobalt", new ConfiguredMaterial("cobalt", "#ff1d41c4", "#c:cobalt_ingot", "#c:cobalt_block", 16,
				800, 3, 12, 4,
				22, new int[]{4, 6, 6, 4}, 0.3f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("copper", new ConfiguredMaterial("copper", "#ffc35d12", "#c:copper_ingot", "#c:cobalt_block", 12,
				275, 1, 5.3f, 1,
				12, new int[]{2, 4, 5, 2}, 0.5f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("electrum", new ConfiguredMaterial("electrum", "#ffffe86d", "#c:electrum_ingot", "#c:electrum_block", 16,
				440, 2, 6, 3,
				20, new int[]{5, 3, 3, 5}, 0.3f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("iridium", new ConfiguredMaterial("iridium", "#ff80e0c2", "#c:iridium_ingot", "#c:iridium_block", 22,
				600, 3, 6.5f, 5,
				25, new int[]{5, 5, 5, 5}, 2f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("lead", new ConfiguredMaterial("lead", "#ff64536d", "#c:lead_ingot", "#c:lead_block", 20,
				375, 1, 5.25f, 1.5f,
				18, new int[]{2, 4, 4, 2}, 0.6f, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));
		materials.put("osmium", new ConfiguredMaterial("osmium", "#ffafc0ed", "#c:osmium_ingot", "#c:osmium_block", 16,
				500, 2, 4.25f, 2,
				20, new int[]{3, 6, 6, 3}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("palladium", new ConfiguredMaterial("palladium", "#ffc3a5d2", "#c:palladium_ingot", "#c:palladium_block", 16,
				100, 3, 16, 3.5f,
				10, new int[]{3, 6, 8, 3}, 2.5f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("platinum", new ConfiguredMaterial("platinum", "#ffffffff", "#c:platinum_ingot", "#c:platinum_block", 16,
				150, 2, 10, 2,
				12, new int[]{2, 5, 5, 2}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("silver", new ConfiguredMaterial("silver", "#ff96b2ff", "#c:silver_ingot", "#c:silver_block", 25,
				325, 1, 5, 3,
				15, new int[]{3, 4, 5, 3}, 1.75f, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));
		materials.put("steel", new ConfiguredMaterial("steel", "#ff464a57", "#c:steel_ingot", "#c:steel_block", 10,
				600, 2, 6, 3,
				18, new int[]{3, 6, 7, 3}, 1.25f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("tin", new ConfiguredMaterial("tin", "#ff6592dd", "#c:tin_ingot", "#c:tin_block", 16,
				290, 1, 5.1f, 2,
				12, new int[]{3, 4, 5, 3}, 0.5f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("titanium", new ConfiguredMaterial("titanium", "#ff6c6c6c", "#c:titanium_ingot", "#c:titanium_block", 14,
				600, 1, 5, 2.7f,
				25, new int[]{4, 5, 5, 4}, 0.75f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("tungsten", new ConfiguredMaterial("tungsten", "#ff31374a", "#c:tungsten_ingot", "#c:tungsten_block", 16,
				700, 3, 8, 3,
				27, new int[]{5, 5, 5, 5}, 2, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
		materials.put("zinc", new ConfiguredMaterial("zinc", "#ff839c94", "#c:zinc_ingot", "#c:zinc_block", 16,
				200, 1, 4.75f, 1,
				18, new int[]{3, 3, 3, 3}, 0.75f, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	}
}
