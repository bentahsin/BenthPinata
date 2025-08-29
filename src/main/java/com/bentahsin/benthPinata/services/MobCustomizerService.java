package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.BenthPinata;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NbtApiException;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Piñata olarak oluşturulan mob'ları, config'deki mob-options'a göre özelleştirir.
 * 1.13.2 API'si için sadeleştirilmiş ve temizlenmiştir.
 */
public class MobCustomizerService {

    private final BenthPinata plugin;

    public MobCustomizerService(BenthPinata plugin) {
        this.plugin = plugin;
    }

    public void applyOptions(LivingEntity entity, Map<String, Object> options) {
        if (options == null || options.isEmpty()) {
            return;
        }

        applyGeneralOptions(entity, options);
        applyEquipmentOptions(entity, options);

        if (entity instanceof Sheep sheep) {
            applySheepOptions(sheep, options);
        } else if (entity instanceof Horse horse) {
            applyHorseOptions(horse, options);
        } else if (entity instanceof Llama llama) {
            applyLlamaOptions(llama, options);
        } else if (entity instanceof Parrot parrot) {
            applyParrotOptions(parrot, options);
        } else if (entity instanceof Ocelot ocelot) {
            applyOcelotOptions(ocelot, options);
        } else if (entity instanceof Creeper creeper) {
            applyCreeperOptions(creeper, options);
        } else if (entity instanceof Villager villager) {
            applyVillagerOptions(villager, options);
        } else if (entity instanceof ZombieVillager zVillager) {
            applyZombieVillagerOptions(zVillager, options);
        }

        if (options.get("nbt-data") instanceof String nbtData) {
            try {
                NBT.modify(entity, nbt -> {
                    ReadWriteNBT parsedNbt = NBT.parseNBT(nbtData);
                    if (parsedNbt instanceof NBTCompound customNbt) {
                        nbt.mergeCompound(customNbt);
                    } else {
                        plugin.getLogger().warning("nbt-data için sağlanan veri bir NBT Compound ('{...}') değil. İşlem atlandı.");
                    }
                });
            } catch (NbtApiException e) {
                plugin.getLogger().warning("'" + nbtData + "' NBT verisi uygulanırken bir hata oluştu. Formatı kontrol edin.");
            }
        }
    }

    private void applyGeneralOptions(LivingEntity entity, Map<String, Object> options) {
        if (options.get("glowing") instanceof Boolean glowing) {
            entity.setGlowing(glowing);
        }
        if (options.get("custom-name") instanceof String name) {
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (options.get("custom-name-visible") instanceof Boolean visible) {
            entity.setCustomNameVisible(visible);
        }
        if (entity instanceof Ageable ageable && options.get("is-baby") instanceof Boolean isBaby) {
            if (isBaby) ageable.setBaby();
            else ageable.setAdult();
        }
    }

    private void applyEquipmentOptions(LivingEntity entity, Map<String, Object> options) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return;

        if (options.get("helmet") instanceof String materialName) equipment.setHelmet(createItem(materialName));
        if (options.get("chestplate") instanceof String materialName) equipment.setChestplate(createItem(materialName));
        if (options.get("leggings") instanceof String materialName) equipment.setLeggings(createItem(materialName));
        if (options.get("boots") instanceof String materialName) equipment.setBoots(createItem(materialName));
        if (options.get("main-hand") instanceof String materialName) equipment.setItemInMainHand(createItem(materialName));
    }

    // --- 1.13.2 Uyumlu Metotlar ---

    private void applyOcelotOptions(Ocelot ocelot, Map<String, Object> options) {
        if (options.get("ocelot-type") instanceof String typeName) {
            try {
                ocelot.setCatType(Ocelot.Type.valueOf(typeName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz ocelot/kedi türü: '" + typeName + "'. Türler: WILD_OCELOT, BLACK_CAT, RED_CAT, SIAMESE_CAT");
            }
        }
    }

    private void applyZombieVillagerOptions(ZombieVillager zVillager, Map<String, Object> options) {
        if (options.get("villager-profession") instanceof String profName) {
            try {
                zVillager.setVillagerProfession(Villager.Profession.valueOf(profName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz köylü zombi mesleği: '" + profName + "'.");
            }
        }
    }

    private void applyLlamaOptions(Llama llama, Map<String, Object> options) {
        if (options.get("llama-color") instanceof String colorName) {
            try {
                llama.setColor(Llama.Color.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz lama rengi: '" + colorName + "'.");
            }
        }
    }

    private void applySheepOptions(Sheep sheep, Map<String, Object> options) {
        if (options.get("color") instanceof String colorName) {
            try {
                sheep.setColor(DyeColor.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz koyun rengi: '" + colorName + "'.");
            }
        }
    }

    private void applyHorseOptions(Horse horse, Map<String, Object> options) {
        if (options.get("horse-color") instanceof String colorName) {
            try {
                horse.setColor(Horse.Color.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz at rengi: '" + colorName + "'.");
            }
        }
        if (options.get("horse-style") instanceof String styleName) {
            try {
                horse.setStyle(Horse.Style.valueOf(styleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz at stili: '" + styleName + "'.");
            }
        }
    }

    private void applyParrotOptions(Parrot parrot, Map<String, Object> options) {
        if (options.get("variant") instanceof String variantName) {
            try {
                parrot.setVariant(Parrot.Variant.valueOf(variantName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz papağan varyantı: '" + variantName + "'.");
            }
        }
    }

    private void applyCreeperOptions(Creeper creeper, Map<String, Object> options) {
        if (options.get("is-powered") instanceof Boolean isPowered) {
            creeper.setPowered(isPowered);
        }
    }

    private void applyVillagerOptions(Villager villager, Map<String, Object> options) {
        if (options.get("profession") instanceof String profName) {
            try {
                villager.setProfession(Villager.Profession.valueOf(profName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz köylü mesleği: '" + profName + "'.");
            }
        }
    }


    private ItemStack createItem(String materialName) {
        try {
            return new ItemStack(Material.valueOf(materialName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz materyal adı: '" + materialName + "'. Eşya oluşturulamadı.");
            return null;
        }
    }
}