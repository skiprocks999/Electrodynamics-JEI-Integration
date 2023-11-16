package electrodynamics.common.tile.machines.furnace;

import java.util.List;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnace;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnaceDouble;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnaceTriple;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

public class TileElectricFurnace extends GenericTile implements ITickableSound {

	protected SmeltingRecipe[] cachedRecipe = null;

	private List<SmeltingRecipe> cachedRecipes = null;

	private boolean isSoundPlaying = false;

	public TileElectricFurnace(BlockPos worldPosition, BlockState blockState) {
		this(SubtypeMachine.electricfurnace, 0, worldPosition, blockState);
	}

	public TileElectricFurnace(SubtypeMachine machine, int extra, BlockPos worldPosition, BlockState blockState) {
		super(extra == 1 ? ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACEDOUBLE.get() : extra == 2 ? ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACETRIPLE.get() : ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACE.get(), worldPosition, blockState);

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, extra)).maxJoules(Constants.ELECTRICFURNACE_USAGE_PER_TICK * 20 * (extra + 1)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, 0).upgrades(3)).validUpgrades(ContainerElectricFurnace.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> (extra == 0 ? new ContainerElectricFurnace(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerElectricFurnaceDouble(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerElectricFurnaceTriple(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(this::canProcess).process(this::process).requiredTicks(Constants.ELECTRICFURNACE_REQUIRED_TICKS).usage(Constants.ELECTRICFURNACE_USAGE_PER_TICK));
		}
		cachedRecipe = new SmeltingRecipe[extra + 1];
	}

	protected void process(ComponentProcessor component) {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack output = inv.getOutputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem(level.registryAccess());
		int index = inv.getOutputSlots().get(component.getProcessorNumber());
		if (!output.isEmpty()) {
			output.setCount(output.getCount() + result.getCount());
			inv.setItem(index, output);
		} else {
			inv.setItem(index, result.copy());
		}
		input.shrink(1);
		inv.setItem(inv.getInputSlotsForProcessor(component.getProcessorNumber()).get(0), input.copy());
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty() && ((ItemUpgrade) stack.getItem()).subtype == SubtypeItemUpgrade.experience) {
				CompoundTag tag = stack.getOrCreateTag();
				tag.putDouble(NBTUtils.XP, tag.getDouble(NBTUtils.XP) + cachedRecipe[component.getProcessorNumber()].getExperience());
				break;
			}
		}
	}

	protected boolean canProcess(ComponentProcessor component) {
		boolean canProcess = checkConditions(component);

		if (BlockEntityUtils.isLit(this) ^ canProcess || isProcessorActive()) {
			BlockEntityUtils.updateLit(this, canProcess || isProcessorActive());
		}

		return canProcess;
	}

	private boolean checkConditions(ComponentProcessor component) {
		component.setShouldKeepProgress(true);
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		if (input.isEmpty()) {
			component.setShouldKeepProgress(false);
			return false;
		}

		if (cachedRecipes == null) {
			cachedRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
		}

		if (cachedRecipe == null) {
			component.setShouldKeepProgress(false);
			return false;
		}

		if (cachedRecipe[component.getProcessorNumber()] == null) {
			cachedRecipe[component.getProcessorNumber()] = getMatchedRecipe(input);
			if (cachedRecipe[component.getProcessorNumber()] == null) {
				component.setShouldKeepProgress(false);
				return false;
			}
			component.operatingTicks.set(0.0);
		}

		if (!cachedRecipe[component.getProcessorNumber()].matches(new SimpleContainer(input), level)) {
			cachedRecipe[component.getProcessorNumber()] = null;
			component.setShouldKeepProgress(false);
			return false;
		}

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		if (electro.getJoulesStored() < component.getUsage() * component.operatingSpeed.get()) {
			return false;
		}
		electro.maxJoules(component.getUsage() * component.operatingSpeed.get() * 10 * component.totalProcessors);

		ItemStack output = inv.getOutputContents().get(component.getProcessorNumber());
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem(level.registryAccess());
		return (output.isEmpty() || output.getItem() == result.getItem()) && output.getCount() + result.getCount() <= output.getMaxStackSize();

	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}
		if (level.random.nextDouble() < 0.15) {
			Direction direction = getFacing();
			double d4 = level.random.nextDouble();
			double d5 = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : d4;
			double d6 = level.random.nextDouble();
			double d7 = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : d4;
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + d5, worldPosition.getY() + d6, worldPosition.getZ() + d7, 0.0D, 0.0D, 0.0D);
		}
		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_HUM.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return isProcessorActive();
	}

	private SmeltingRecipe getMatchedRecipe(ItemStack stack) {
		for (SmeltingRecipe recipe : cachedRecipes) {
			if (recipe.matches(new SimpleContainer(stack), level)) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	public int getComparatorSignal() {
		return (int) (((double) getNumActiveProcessors() / (double) Math.max(1, getNumProcessors())) * 15.0);
	}

}
