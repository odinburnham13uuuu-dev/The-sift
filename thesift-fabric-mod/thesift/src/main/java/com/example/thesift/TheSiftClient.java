package com.example.thesift;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class TheSiftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Standard translucent layer: shader packs (Iris/Optifine) treat it like stained glass/portals.
        BlockRenderLayerMap.putBlock(ModBlocks.RIFT_PORTAL, ChunkSectionLayer.TRANSLUCENT);
    }
}
