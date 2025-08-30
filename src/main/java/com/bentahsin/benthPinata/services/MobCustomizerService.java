package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.BenthPinata;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NbtApiException;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
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

        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep) entity;
            applySheepOptions(sheep, options);
        } else if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            applyHorseOptions(horse, options);
        } else if (entity instanceof Llama) {
            Llama llama = (Llama) entity;
            applyLlamaOptions(llama, options);
        } else if (entity instanceof Parrot) {
            Parrot parrot = (Parrot) entity;
            applyParrotOptions(parrot, options);
        } else if (entity instanceof Ocelot) {
            Ocelot ocelot = (Ocelot) entity;
            applyOcelotOptions(ocelot, options);
        } else if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            applyCreeperOptions(creeper, options);
        } else if (entity instanceof Villager) {
            Villager villager = (Villager) entity;
            applyVillagerOptions(villager, options);
        } else if (entity instanceof ZombieVillager) {
            ZombieVillager zVillager = (ZombieVillager) entity;
            applyZombieVillagerOptions(zVillager, options);
        }

        if (options.get("nbt-data") instanceof String) {
            String nbtData = (String) options.get("nbt-data");
            try {
                NBT.modify(entity, nbt -> {
                    ReadWriteNBT parsedNbt = NBT.parseNBT(nbtData);
                    if (parsedNbt instanceof NBTCompound) {
                        NBTCompound customNbt = (NBTCompound) parsedNbt;
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
        if (options.get("glowing") instanceof Boolean) {
            boolean glowing = (Boolean) options.get("glowing");
            entity.setGlowing(glowing);
        }
        if (options.get("custom-name") instanceof String) {
            String name = (String) options.get("custom-name");
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (options.get("custom-name-visible") instanceof Boolean) {
            boolean visible = (Boolean) options.get("custom-name-visible");
            entity.setCustomNameVisible(visible);
        }
        if (entity instanceof Ageable && options.get("is-baby") instanceof Boolean) {
            Ageable ageable = (Ageable) entity;
            boolean isBaby = (Boolean) options.get("is-baby");
            if (isBaby) ageable.setBaby();
            else ageable.setAdult();
        }
    }

    private void applyEquipmentOptions(LivingEntity entity, Map<String, Object> options) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return;

        if (options.get("helmet") instanceof String) {
            String materialName = (String) options.get("helmet");
            equipment.setHelmet(createItem(materialName));
        }
        if (options.get("chestplate") instanceof String) {
            String materialName = (String) options.get("chestplate");
            equipment.setChestplate(createItem(materialName));
        }
        if (options.get("leggings") instanceof String) {
            String materialName = (String) options.get("leggings");
            equipment.setLeggings(createItem(materialName));
        }
        if (options.get("boots") instanceof String) {
            String materialName = (String) options.get("boots");
            equipment.setBoots(createItem(materialName));
        }
        if (options.get("main-hand") instanceof String) {
            String materialName = (String) options.get("main-hand");
            equipment.setItemInMainHand(createItem(materialName));
        }
    }

    // --- 1.13.2 Uyumlu Metotlar ---

    private void applyOcelotOptions(Ocelot ocelot, Map<String, Object> options) {
        if (options.get("ocelot-type") instanceof String) {
            String typeName = (String) options.get("ocelot-type");
            try {
                ocelot.setCatType(Ocelot.Type.valueOf(typeName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz ocelot/kedi türü: '" + typeName + "'. Türler: WILD_OCELOT, BLACK_CAT, RED_CAT, SIAMESE_CAT");
            }
        }
    }

    private void applyZombieVillagerOptions(ZombieVillager zVillager, Map<String, Object> options) {
        if (options.get("villager-profession") instanceof String) {
            String profName = (String) options.get("villager-profession");
            try {
                zVillager.setVillagerProfession(Villager.Profession.valueOf(profName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz köylü zombi mesleği: '" + profName + "'.");
            }
        }
    }

    private void applyLlamaOptions(Llama llama, Map<String, Object> options) {
        if (options.get("llama-color") instanceof String) {
            String colorName = (String) options.get("llama-color");
            try {
                llama.setColor(Llama.Color.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz lama rengi: '" + colorName + "'.");
            }
        }
    }

    private void applySheepOptions(Sheep sheep, Map<String, Object> options) {
        if (options.get("color") instanceof String) {
            String colorName = (String) options.get("color");
            try {
                sheep.setColor(DyeColor.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz koyun rengi: '" + colorName + "'.");
            }
        }
    }

    private void applyHorseOptions(Horse horse, Map<String, Object> options) {
        if (options.get("horse-color") instanceof String) {
            String colorName = (String) options.get("horse-color");
            try {
                horse.setColor(Horse.Color.valueOf(colorName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz at rengi: '" + colorName + "'.");
            }
        }
        if (options.get("horse-style") instanceof String) {
            String styleName = (String) options.get("horse-style");
            try {
                horse.setStyle(Horse.Style.valueOf(styleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz at stili: '" + styleName + "'.");
            }
        }
    }

    private void applyParrotOptions(Parrot parrot, Map<String, Object> options) {
        if (options.get("variant") instanceof String) {
            String variantName = (String) options.get("variant");
            try {
                parrot.setVariant(Parrot.Variant.valueOf(variantName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Geçersiz papağan varyantı: '" + variantName + "'.");
            }
        }
    }

    private void applyCreeperOptions(Creeper creeper, Map<String, Object> options) {
        if (options.get("is-powered") instanceof Boolean) {
            boolean isPowered = (Boolean) options.get("is-powered");
            creeper.setPowered(isPowered);
        }
    }

    private void applyVillagerOptions(Villager villager, Map<String, Object> options) {
        if (options.get("profession") instanceof String ) {
            String profName = (String) options.get("profession");
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