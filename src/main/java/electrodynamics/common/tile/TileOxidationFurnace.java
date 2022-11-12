package electrodynamics.common.tile;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerDO2OProcessor;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.InventoryUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.UnifiedElectrodynamicsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TileOxidationFurnace extends GenericTile {

	public TileOxidationFurnace(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsBlockTypes.TILE_OXIDATIONFURNACE.get(), worldPosition, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2));
		addComponent(new ComponentInventory(this).size(7).faceSlots(Direction.UP, 0, 1).relativeFaceSlots(Direction.EAST, 1).relativeSlotFaces(2, Direction.DOWN, Direction.WEST).inputs(2).outputs(1).biproducts(1).upgrades(3).processorInputs(2).validUpgrades(ContainerDO2OProcessor.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider("container.oxidationfurnace").createMenu((id, player) -> new ContainerDO2OProcessor(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
		addComponent(new ComponentProcessor(this).setProcessorNumber(0).canProcess(this::canProcessOxideFurn).process(component -> component.processItem2ItemRecipe(component)).requiredTicks(Constants.OXIDATIONFURNACE_REQUIRED_TICKS).usage(Constants.OXIDATIONFURNACE_USAGE_PER_TICK));
	}

	protected void tickServer(ComponentTickable tick) {
		InventoryUtils.handleExperienceUpgrade(this);
	}

	protected boolean canProcessOxideFurn(ComponentProcessor component) {
		if (component.canProcessItem2ItemRecipe(component, ElectrodynamicsRecipeInit.OXIDATION_FURNACE_TYPE.get())) {
			if (getBlockState().getBlock() == UnifiedElectrodynamicsRegister.getSafeBlock(SubtypeMachine.oxidationfurnace)) {
				level.setBlock(worldPosition, UnifiedElectrodynamicsRegister.getSafeBlock(SubtypeMachine.oxidationfurnacerunning).defaultBlockState().setValue(GenericEntityBlock.FACING, getBlockState().getValue(GenericEntityBlock.FACING)).setValue(BlockStateProperties.WATERLOGGED, getBlockState().getValue(BlockStateProperties.WATERLOGGED)), 2 | 16 | 32);
			}
			return true;
		} else if (getBlockState().getBlock() == UnifiedElectrodynamicsRegister.getSafeBlock(SubtypeMachine.oxidationfurnacerunning)) {
			level.setBlock(worldPosition, UnifiedElectrodynamicsRegister.getSafeBlock(SubtypeMachine.oxidationfurnace).defaultBlockState().setValue(GenericEntityBlock.FACING, getBlockState().getValue(GenericEntityBlock.FACING)).setValue(BlockStateProperties.WATERLOGGED, getBlockState().getValue(BlockStateProperties.WATERLOGGED)), 2 | 16 | 32);
		}
		return false;
	}

	protected void tickClient(ComponentTickable tickable) {
		ComponentProcessor processor = getComponent(ComponentType.Processor);
		if (processor.operatingTicks > 0 && level.random.nextDouble() < 0.15) {
			Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
			double d4 = level.random.nextDouble();
			double d5 = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : d4;
			double d6 = level.random.nextDouble();
			double d7 = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : d4;
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + d5, worldPosition.getY() + d6, worldPosition.getZ() + d7, 0.0D, 0.0D, 0.0D);
		}
		if (processor.operatingTicks > 0 && tickable.getTicks() % 200 == 0) {
			SoundAPI.playSound(ElectrodynamicsSounds.SOUND_HUM.get(), SoundSource.BLOCKS, 1, 1, worldPosition);
		}
	}

}
