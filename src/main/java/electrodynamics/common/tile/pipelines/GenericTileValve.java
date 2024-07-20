package electrodynamics.common.tile.pipelines;

import net.minecraft.core.HolderLookup;

import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GenericTileValve extends GenericTile {

	public static final Direction INPUT_DIR = Direction.SOUTH;
	public static final Direction OUTPUT_DIR = Direction.NORTH;

	public boolean isClosed = false;

	protected boolean isLocked = false;

	public GenericTileValve(BlockEntityType<?> tile, BlockPos pos, BlockState state) {
		super(tile, pos, state);
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}

		if (level.hasNeighborSignal(worldPosition)) {
			isClosed = true;
		} else {
			isClosed = false;
		}

		if (BlockEntityUtils.isLit(this) ^ isClosed) {
			BlockEntityUtils.updateLit(this, isClosed);
		}

	}

	@Override
	public void onPlace(BlockState oldState, boolean isMoving) {
		super.onPlace(oldState, isMoving);
		if (level.isClientSide) {
			return;
		}
		if (level.hasNeighborSignal(worldPosition)) {
			isClosed = true;
		} else {
			isClosed = false;
		}

		if (BlockEntityUtils.isLit(this) ^ isClosed) {
			BlockEntityUtils.updateLit(this, isClosed);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);

		compound.putBoolean("valveisclosed", isClosed);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		isClosed = compound.getBoolean("valveisclosed");
	}
}
