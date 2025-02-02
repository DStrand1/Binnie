package binnie.extratrees;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

import sun.misc.Unsafe;
import binnie.core.block.TileEntityMetadata;
import forestry.api.arboriculture.EnumWoodType;
import forestry.arboriculture.tiles.TileWood;

public class FakeWorld extends World {

    private static Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static FakeWorld instance = SilentObjectCreator.create(FakeWorld.class);

    protected Block block;
    protected int metadata;
    protected TileEntity te;

    // It is never called.
    private FakeWorld() {
        super(null, "", null, new WorldSettings(Long.MAX_VALUE, GameType.NOT_SET, false, false, WorldType.FLAT), null);
    }

    public ItemStack getWooLog() {
        if (te instanceof TileWood) {
            return TileWood.getPickBlock(block, this, 0, 0, 0);
        } else if (te instanceof TileEntityMetadata) {
            return new ItemStack(block, 1, ((TileEntityMetadata) te).getTileMetadata());
        }
        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block) {
        this.block = block;
        return true;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int metadata, int update) {
        this.block = block;
        this.metadata = metadata;
        if (block.hasTileEntity(metadata)) {
            te = block.createTileEntity(this, metadata);
        } else {
            TileWood wood = new TileWood();
            wood.setWoodType(EnumWoodType.VALUES[metadata]);
            te = wood;
        }
        return true;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z) {
        return true;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return te;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity tileEntity) {}

    @Override
    public Block getBlock(int x, int y, int z) {
        return block != null ? block : Blocks.air;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        return metadata;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int metadata, int delay) {
        this.metadata = metadata;
        return true;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected int func_152379_p() {
        return 0;
    }

    @Override
    public Entity getEntityByID(int id) {
        return null;
    }

    public static class SilentObjectCreator {

        /**
         * Creating objects without calling constructors.
         */
        public static <T> T create(Class<T> clazz) {
            try {
                return clazz.cast(unsafe.allocateInstance(clazz));
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
