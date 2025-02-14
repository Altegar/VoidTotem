package com.affehund.voidtotem;

import com.affehund.voidtotem.core.VoidTotemConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;

public class VoidTotemFabric implements ModInitializer {

    public static final Item VOID_TOTEM_ITEM = new Item(
            new FabricItemSettings().maxCount(1).group(CreativeModeTab.TAB_COMBAT).rarity(Rarity.UNCOMMON));

    public static VoidTotemConfig CONFIG;

    public static final SimpleParticleType VOID_TOTEM_PARTICLE = FabricParticleTypes.simple();

    @Override
    public void onInitialize() {

        VoidTotem.init();

        Registry.register(Registry.ITEM, new ResourceLocation(ModConstants.MOD_ID, ModConstants.ITEM_VOID_TOTEM),
                VOID_TOTEM_ITEM);

        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(ModConstants.MOD_ID, "void_totem"), VOID_TOTEM_PARTICLE);

        AutoConfig.register(VoidTotemConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(VoidTotemConfig.class).getConfig();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (CONFIG.ADD_END_CITY_TREASURE && id.equals(BuiltInLootTables.END_CITY_TREASURE)) {
                var pools = List.of(lootManager.get(ModConstants.END_CITY_TREASURE_INJECTION_LOCATION).pools);
                tableBuilder.pools(pools);
            }
        });
    }
}
