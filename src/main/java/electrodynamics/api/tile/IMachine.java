package electrodynamics.api.tile;

import electrodynamics.api.multiblock.subnodebased.Subnode;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IMachine {

    public BlockEntityType.BlockEntitySupplier<BlockEntity> getBlockEntitySupplier();

    public int getLitBrightness();

    public RenderShape getRenderShape();

    public boolean isMultiblock();

    public boolean propegatesLightDown();

    public boolean isPlayerStorable();

    public default Subnode[] getSubnodes() {
        return new Subnode[]{};
    }

    public VoxelShapeProvider getVoxelShapeProvider();

    public boolean usesLit();

}
