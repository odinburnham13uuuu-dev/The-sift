package com.example.thesift;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Portal layout (4 wide x 5 tall, like a Nether portal):
 *   N B B N      N = note block, tuned to note 3 (right-click 3 times)
 *   B . . B      B = Carapace Bone
 *   B . . B      . = empty (becomes the rift)
 *   B . . B
 *   N B B N
 */
public final class RiftPortal {
    public static final int W = 4, H = 5;

    private RiftPortal() {}

    private static BlockPos at(BlockPos o, Direction d, int i, int j) {
        return o.relative(d, i).above(j);
    }

    public static boolean tryLight(ServerLevel level, BlockPos notePos) {
        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            Direction d = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
            for (int ci : new int[]{0, W - 1}) {
                for (int cj : new int[]{0, H - 1}) {
                    BlockPos origin = notePos.relative(d.getOpposite(), ci).below(cj);
                    if (valid(level, origin, d)) {
                        light(level, origin, d, axis);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean valid(ServerLevel level, BlockPos o, Direction d) {
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                BlockState s = level.getBlockState(at(o, d, i, j));
                boolean edgeI = i == 0 || i == W - 1, edgeJ = j == 0 || j == H - 1;
                if (edgeI && edgeJ) {
                    if (!(s.is(Blocks.NOTE_BLOCK) && s.getValue(NoteBlock.NOTE) == 3)) return false;
                } else if (edgeI || edgeJ) {
                    if (!s.is(ModBlocks.CARAPACE_BONE)) return false;
                } else if (!s.isAir()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void light(ServerLevel level, BlockPos o, Direction d, Direction.Axis axis) {
        BlockState portal = ModBlocks.RIFT_PORTAL.defaultBlockState().setValue(RiftPortalBlock.AXIS, axis);
        for (int i = 1; i < W - 1; i++)
            for (int j = 1; j < H - 1; j++)
                level.setBlock(at(o, d, i, j), portal, 3);
        level.playSound(null, o.relative(d, 1).above(2), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 2.0f, 0.8f);
    }

    /** Builds a finished portal (used for the return portal on the other side). */
    public static void build(ServerLevel level, BlockPos o, Direction d, Direction.Axis axis) {
        // clear walking space around the frame
        for (int i = -1; i <= W; i++)
            for (int k = -1; k <= 1; k++)
                for (int j = 1; j < H; j++) {
                    BlockPos p = o.relative(d, i).above(j).relative(d.getClockWise(), k);
                    level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
                }
        BlockState bone = ModBlocks.CARAPACE_BONE.defaultBlockState();
        for (int i = 0; i < W; i++)
            for (int j = 0; j < H; j++) {
                boolean edge = i == 0 || i == W - 1 || j == 0 || j == H - 1;
                if (edge) level.setBlock(at(o, d, i, j), bone, 3);
            }
        light(level, o, d, axis);
    }
}
