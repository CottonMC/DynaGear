package io.github.cottonmc.dynagear;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import io.github.cottonmc.dynagear.api.ConfiguredMaterial;
import io.github.cottonmc.jankson.JanksonFactory;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MaterialConfig {

	public static Jankson getJankson() {
		return JanksonFactory.createJankson();
	}

	public static void loadConfig() {
		try {
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("dynagear.json5").toFile();
			if (!file.exists()) {
				DynaGear.logger.warn("[DynaGear] No config file found! Generating an empty file.");
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file, false);
				out.write("{ }".getBytes());
				out.flush();
				out.close();
				return;
			}
			JsonObject json = getJankson().load(file);
			List<String> keys = new ArrayList<>(json.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				JsonElement elem = json.get(key);
				if (elem instanceof JsonObject) {
					JsonObject config = (JsonObject)elem;
					String material = config.get(String.class, "material");
					if (material == null) {
						DynaGear.logger.error("[DynaGear] Could not find ingredient material for material " + key + "! Skipping!");
						continue;
					}
					ConfiguredMaterial mat = getMaterial(key, config);
					DynaGear.MATERIALS.put(new Identifier(DynaGear.MODID, key), mat);
				}
			}
		} catch (IOException | SyntaxError e) {
			DynaGear.logger.error("[DynaGear] Error loading config: {}", e.getMessage());
		}
	}

	public static void loadData() {
		Set<StaticDataItem> data = StaticData.getAll("dynagear.json5");
		for (StaticDataItem file : data) {
			try {
				JsonObject json = getJankson().load(file.createInputStream());
				List<String> keys = new ArrayList<>(json.keySet());
				Collections.sort(keys);
				for (String key : keys) {
					JsonElement elem = json.get(key);
					if (elem instanceof JsonObject) {
						JsonObject config = (JsonObject)elem;
						String material = config.get(String.class, "material");
						if (material == null) {
							DynaGear.logger.error("[DynaGear] Could not find ingredient material for material {} in {}! Skipping!", key, file.getIdentifier().toString());
							continue;
						}
						ConfiguredMaterial mat = getMaterial(key, config);
						Identifier matId = new Identifier(DynaGear.MODID, key);
						if (!DynaGear.MATERIALS.containsKey(matId)) DynaGear.MATERIALS.put(matId, mat);
						else DynaGear.logger.error("[DynaGear] Skipping materian {} in {}, as it already exists", key, file.getIdentifier().toString());
					}
				}
			} catch (IOException | SyntaxError e) {
				DynaGear.logger.error("[DynaGear] Error loading data file {}: {}", file.getIdentifier().toString(), e.getMessage());
			}
		}
	}

	public static ConfiguredMaterial getMaterial(String name, JsonObject json) throws SyntaxError {
		String color = json.get(String.class, "color");
		String material = json.get(String.class, "material");
		String blockMaterial = json.get(String.class, "block_material");
		Integer enchantability = json.get(Integer.class, "enchantability");
		Integer toolDurability = json.get(Integer.class, "tool_durability");
		Integer miningLevel = json.get(Integer.class, "mining_level");
		Float miningSpeed = json.get(Float.class, "mining_speed");
		Float attackDamage = json.get(Float.class, "attack_damage");
		Integer armorMultiplier = json.get(Integer.class, "armor_multiplier");
		JsonArray protectionAmounts = (JsonArray)json.get("protection_amounts");
		Float toughness = json.get(Float.class, "armor_toughness");
		Float knockbackResistance = json.get(Float.class, "armor_knockback_resistance");
		String soundId = json.get(String.class, "equip_sound");

		if (color == null) throw new SyntaxError("No color for material " + name);
		if (material == null) throw new SyntaxError("No material ingredient for material " + name);
		if (blockMaterial == null) blockMaterial = "";
		if (enchantability == null) throw new SyntaxError("No enchantability for material " + name);
		if (toolDurability == null) throw new SyntaxError("No tool durability for material " + name);
		if (miningLevel == null) throw new SyntaxError("No mining level for material " + name);
		if (miningSpeed == null) throw new SyntaxError("No mining speed for material" + name);
		if (attackDamage == null) throw new SyntaxError("No attack damage for material " + name);
		if (armorMultiplier == null) throw new SyntaxError("No armor multiplier for material " + name);
		if (protectionAmounts == null) throw new SyntaxError("No protection amounts for material " + name);
		if (toughness == null) throw new SyntaxError("No armor toughness for material " + name);
		if (knockbackResistance == null) knockbackResistance=0f;
		if (soundId == null) throw new SyntaxError("No equip sound for material " + name);

		IntList prots = new IntArrayList();
		for (JsonElement el : protectionAmounts) {
			if (el instanceof JsonPrimitive) {
				Object val = (((JsonPrimitive) el).getValue());
				if (val instanceof Integer) {
					prots.add((int)val);
				} else if (val instanceof Long) {
					prots.add(((Long)val).intValue());
				}
			}
		}

		SoundEvent sound = Registry.SOUND_EVENT.get(new Identifier(soundId));
		return new ConfiguredMaterial(name, color, material, blockMaterial, enchantability, toolDurability, miningLevel, miningSpeed, attackDamage, armorMultiplier, prots.toArray(new int[4]), toughness, knockbackResistance, sound);
	}
}
