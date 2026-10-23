package com.example.thesift;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    public static final CreativeModeTab TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, TheSiftMod.id("the_sift")),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.thesift"))
                    .icon(() -> new ItemStack(ModBlocks.SOUL_CRYSTAL))
                    .displayItems((params, out) -> {
                        out.accept(ModBlocks.SIFT_SOIL);
                        out.accept(ModBlocks.SIFT_STONE);
                        out.accept(ModBlocks.CARAPACE_BONE);
                        out.accept(ModBlocks.CARAPACE_SAND);
                        out.accept(ModBlocks.SOUL_CRYSTAL);
                        out.accept(Items.NOTE_BLOCK); // convenience: portal ritual needs 4
                    })
                    .build());

    public static void init() {}
}
