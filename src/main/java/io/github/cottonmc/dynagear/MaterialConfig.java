package io.github.cottonmc.dynagear;

import blue.endless.jankson.*;
import blue.endless.jankson.impl.SyntaxError;
import com.google.gson.JsonSyntaxException;
import io.github.cottonmc.jankson.JanksonFactory;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public class MaterialConfig {
	public static final Map<String, EquipmentSet> EQUIPMENT = new HashMap<>();
	public static final Jankson jankson = JanksonFactory.createJankson();

	public static void loadConfig() {
		try {
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("dynagear.json5").toFile();
			if (!file.exists()) {
				if (FabricLoader.getInstance().isModLoaded("cotton-resources")) {
					DynaGear.logger.info("Cotton Resources detected! Generating default equipment...");
					exportResourcesConfig();
				} else {
					DynaGear.logger.warn("No config was found! DynaGear will not add any equipment!");
					return;
				}
			}
			JsonObject json = jankson.load(file);
			List<String> keys = new ArrayList<>(json.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				JsonElement elem = json.get(key);
				if (elem instanceof JsonObject) {
					JsonObject config = (JsonObject)elem;
					String material = config.get(String.class, "material");
					if (material == null) {
						DynaGear.logger.error("Could not find ingredient material for material " + key + "! Skipping!");
						continue;
					}
					ConfiguredMaterial mat = getMaterial(key, config);
					EQUIPMENT.put(key, EquipmentSet.create(mat));
				}
			}
		} catch (IOException | SyntaxError e) {
			DynaGear.logger.error("Error loading config: {}", e.getMessage());
		}
	}

	public static ConfiguredMaterial getMaterial(String name, JsonObject json) throws SyntaxError {
		String color = json.get(String.class, "color");
		String material = json.get(String.class, "material");
		Integer enchantability = json.get(Integer.class, "enchantability");
		Integer toolDurability = json.get(Integer.class, "tool_durability");
		Integer miningLevel = json.get(Integer.class, "mining_level");
		Float miningSpeed = json.get(Float.class, "mining_speed");
		Float attackDamage = json.get(Float.class, "attack_damage");
		Integer armorMultiplier = json.get(Integer.class, "armor_multiplier");
		JsonArray protectionAmounts = (JsonArray)json.get("protection_amounts");
		Float toughness = json.get(Float.class, "armor_toughness");
		String soundId = json.get(String.class, "equip_sound");

		if (color == null) throw new SyntaxError("No color for material " + name);
		if (material == null) throw new SyntaxError("No material ingredient for material " + name);
		if (enchantability == null) throw new SyntaxError("No enchantability for material " + name);
		if (toolDurability == null) throw new SyntaxError("No tool durability for material " + name);
		if (miningLevel == null) throw new SyntaxError("No mining level for material " + name);
		if (miningSpeed == null) throw new SyntaxError("No mining speed for material" + name);
		if (attackDamage == null) throw new SyntaxError("No attack damage for material " + name);
		if (armorMultiplier == null) throw new SyntaxError("No armor multiplier for material " + name);
		if (protectionAmounts == null) throw new SyntaxError("No protection amounts for material " + name);
		if (toughness == null) throw new SyntaxError("No armor toughness for material " + name);
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
		return new ConfiguredMaterial(name, color, material, enchantability, toolDurability, miningLevel, miningSpeed, attackDamage, armorMultiplier, prots.toArray(new int[4]), toughness, sound);
	}

	public static void exportResourcesConfig() {
		File export = FabricLoader.getInstance().getModContainer("dynagear")
				.orElseThrow(IllegalStateException::new).getPath("cr_config.json5").toFile();
		File target = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("dynagear.json5").toFile();
		if (export.exists()) {
			try {
				JsonObject json = jankson.load(export);
				//TODO: once ore-voting is merged, remove elements for non-registered Cotton Resources mats
				String result = json.toJson(true, true);
				if (!target.exists()) target.createNewFile();
				FileOutputStream out = new FileOutputStream(target, false);
				out.write(result.getBytes());
				out.flush();
				out.close();
			} catch (IOException | SyntaxError e) {
				DynaGear.logger.error("Could not find the Cotton Resources config to export!");
			}
		}

	}
}
