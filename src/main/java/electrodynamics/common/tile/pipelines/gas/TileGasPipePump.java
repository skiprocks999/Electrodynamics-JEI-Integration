package electrodynamics.common.tile.pipelines.gas;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.common.inventory.container.tile.ContainerGasPipePump;
import electrodynamics.common.network.type.GasNetwork;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyType;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileGasPipePump extends GenericTile {

	public static final Direction INPUT_DIR = Direction.SOUTH;
	public static final Direction OUTPUT_DIR = Direction.NORTH;

	public final Property<Integer> priority = property(new Property<>(PropertyType.Integer, "pumppriority", 0).onChange((prop, oldval) -> {

		if (level.isClientSide) {
			return;
		}

		BlockEntity entity = level.getBlockEntity(worldPosition.relative(BlockEntityUtils.getRelativeSide(getFacing(), INPUT_DIR)));

		if (entity != null && entity instanceof TileGasPipe pipe) {
			GasNetwork network = pipe.getNetwork();

			if (network != null) {
				network.updateGasPipePumpStats(this, prop.get(), oldval);
			}
		}

	}));

	public TileGasPipePump(BlockPos pos, BlockState state) {
		super(ElectrodynamicsBlockTypes.TILE_GASPIPEPUMP.get(), pos, state);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, false, true).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.PIPE_PUMP_USAGE_PER_TICK * 10).setInputDirections(Direction.WEST));
		addComponent(new ComponentContainerProvider("container.gaspipepump", this).createMenu((id, inv) -> new ContainerGasPipePump(id, inv, getCoordsArray())));
	}

	public void tickServer(ComponentTickable tick) {

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

		electro.joules(Math.max(electro.getJoulesStored() - Constants.PIPE_PUMP_USAGE_PER_TICK, 0));

	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
		if (cap == ElectrodynamicsCapabilities.ELECTRODYNAMIC) {
			return super.getCapability(cap, side);
		}
		if (side == null || cap != ElectrodynamicsCapabilities.GAS_HANDLER) {
			return LazyOptional.empty();
		}
		Direction facing = getFacing();

		if (side == BlockEntityUtils.getRelativeSide(facing, OUTPUT_DIR)) {
			return LazyOptional.of(() -> CapabilityUtils.EMPTY_GAS).cast();
		}

		if (side == BlockEntityUtils.getRelativeSide(facing, INPUT_DIR)) {

			BlockEntity output = level.getBlockEntity(worldPosition.relative(side.getOpposite()));
			if (output == null) {
				return LazyOptional.of(() -> CapabilityUtils.EMPTY_GAS).cast();
			}
			LazyOptional<IGasHandler> lazy = output.getCapability(ElectrodynamicsCapabilities.GAS_HANDLER, side);

			if (lazy.isPresent()) {
				return lazy.cast();
			}
			return LazyOptional.of(() -> CapabilityUtils.EMPTY_GAS).cast();

		}
		return LazyOptional.empty();
	}

	public boolean isPowered() {
		return this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored() >= Constants.PIPE_PUMP_USAGE_PER_TICK;
	}

}
