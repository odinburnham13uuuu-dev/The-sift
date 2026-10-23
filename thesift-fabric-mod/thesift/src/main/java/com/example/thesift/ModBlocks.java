package com.example.thesift;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block SIFT_SOIL = register("sift_soil",
            BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.GRASS));
    public static final Block SIFT_STONE = register("sift_stone",
            BlockBehaviour.Properties.of().strength(1.5f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.STONE));
    public static final Block CARAPACE_BONE = register("carapace_bone",
            BlockBehaviour.Properties.of().strength(2.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.BONE_BLOCK));
    public static final Block CARAPACE_SAND = register("carapace_sand",
            BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.SAND));
    public static final Block SOUL_CRYSTAL = register("soul_crystal",
            BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.AMETHYST).lightLevel(s -> 12));

    public static final Block RIFT_PORTAL = registerPortal();

    private static Block register(String name, BlockBehaviour.Properties props) {
        ResourceKey<Block> bk = ResourceKey.create(Registries.BLOCK, TheSiftMod.id(name));
        Block block = Registry.register(BuiltInRegistries.BLOCK, bk, new Block(props.setId(bk)));
        ResourceKey<Item> ik = ResourceKey.create(Registries.ITEM, TheSiftMod.id(name));
        Registry.register(BuiltInRegistries.ITEM, ik,
                new BlockItem(block, new Item.Properties().setId(ik).useBlockDescriptionPrefix()));
        return block;
    }

    private static Block registerPortal() {
        ResourceKey<Block> bk = ResourceKey.create(Registries.BLOCK, TheSiftMod.id("rift_portal"));
        return Registry.register(BuiltInRegistries.BLOCK, bk, new RiftPortalBlock(
                BlockBehaviour.Properties.of().noCollision().noOcclusion().strength(-1f)
                        .lightLevel(s -> 11).noLootTable().setId(bk)));
    }

    public static void init() {}
}
