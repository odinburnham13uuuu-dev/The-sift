package com.example.thesift;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class TheSiftMod implements ModInitializer {
    public static final String MOD_ID = "thesift";
    public static final ResourceKey<Level> THE_SIFT =
            ResourceKey.create(Registries.DIMENSION, id("the_sift"));

    private static final Map<UUID, Integer> PROGRESS = new HashMap<>();
    private static final Set<UUID> MUST_EXIT = new HashSet<>();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();

        // Ritual: left-clicking ("playing") a note block checks for a finished portal.
        AttackBlockCallback.EVENT.register((player, world, hand, pos, dir) -> {
            BlockState state = world.getBlockState(pos);
            if (!state.is(Blocks.NOTE_BLOCK)) return InteractionResult.PASS;
            if (world instanceof ServerLevel sl) {
                if (player.isCreative()) state.attack(world, pos, player); // play it instead of breaking it
                RiftPortal.tryLight(sl, pos);
            }
            return player.isCreative() ? InteractionResult.FAIL : InteractionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(TheSiftMod::tickPortals);
    }

    private static void tickPortals(MinecraftServer server) {
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            UUID id = p.getUUID();
            boolean inside = p.level().getBlockState(p.blockPosition()).is(ModBlocks.RIFT_PORTAL);
            if (!inside) { PROGRESS.remove(id); MUST_EXIT.remove(id); continue; }
            if (MUST_EXIT.contains(id)) continue;
            int t = PROGRESS.merge(id, 1, Integer::sum);
            if (t >= (p.isCreative() ? 1 : 40)) {
                PROGRESS.remove(id);
                MUST_EXIT.add(id);
                travel(server, p);
            }
        }
    }

    private static void travel(MinecraftServer server, ServerPlayer p) {
        boolean inSift = p.level().dimension().equals(THE_SIFT);
        ServerLevel to = server.getLevel(inSift ? Level.OVERWORLD : THE_SIFT);
        if (to == null) return;
        int x = p.blockPosition().getX(), z = p.blockPosition().getZ();
        to.getChunk(x >> 4, z >> 4);
        int y = to.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

        BlockPos found = findPortal(to, x, y, z);
        Vec3 dest;
        if (found != null) {
            dest = Vec3.atBottomCenterOf(found);
        } else {
            RiftPortal.build(to, new BlockPos(x - 1, y, z), Direction.EAST, Direction.Axis.X);
            dest = new Vec3(x + 1.0, y + 1, z + 0.5);
        }
        p.teleport(new TeleportTransition(to, dest, Vec3.ZERO, p.getYRot(), p.getXRot(), TeleportTransition.DO_NOTHING));
    }

    private static BlockPos findPortal(ServerLevel level, int x, int y, int z) {
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
        for (int dx = -8; dx <= 8; dx++)
            for (int dz = -8; dz <= 8; dz++)
                for (int yy = y - 8; yy <= y + 16; yy++) {
                    m.set(x + dx, yy, z + dz);
                    if (level.getBlockState(m).is(ModBlocks.RIFT_PORTAL)) return m.immutable();
                }
        return null;
    }
}
