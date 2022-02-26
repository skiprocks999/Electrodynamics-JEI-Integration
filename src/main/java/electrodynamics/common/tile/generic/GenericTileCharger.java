package electrodynamics.common.tile.generic;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.inventory.container.tile.ContainerChargerGeneric;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericTileCharger extends GenericTile {
	
	private static final int BATTERY_COUNT = 3;
	private static final double MAX_BATTERY_TRANSFER_JOULES = 1000.0;
	
	protected GenericTileCharger(BlockEntityType<?> typeIn, int voltageMultiplier, String containerName, BlockPos worldPosition, BlockState blockState) {
		super(typeIn, worldPosition, blockState);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler().guiPacketReader(this::loadFromNBT).guiPacketWriter(this::saveToNBT));
		addComponent(new ComponentTickable().tickCommon(this::tickCommon));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * voltageMultiplier).maxJoules(1000.0 * voltageMultiplier));
		addComponent(new ComponentInventory(this).size(2 + BATTERY_COUNT).inputs(1 + BATTERY_COUNT).outputs(1).valid(machineValidator()));
		addComponent(new ComponentContainerProvider("container.charger" + containerName).createMenu((id, player) -> new ContainerChargerGeneric(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));

	}

	public void tickCommon(ComponentTickable tickable) {
		ComponentInventory inventory = getComponent(ComponentType.Inventory);
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		ItemStack itemInput = inventory.getItem(0);
		if (!itemInput.isEmpty() && itemInput.getItem() instanceof IItemElectric electricItem) {
			boolean hasOvervolted = false;
			if(inventory.inputs() > 1) {
				hasOvervolted = drainBatterySlots(inventory, electro);
			}
			double room = electricItem.getElectricProperties().capacity - electricItem.getJoulesStored(itemInput);
			//Electrodynamics.LOGGER.info(electro.getJoulesStored());
			if(electro.getJoulesStored() > 0 && !hasOvervolted && room > 0) {
				double recieveVoltage = electricItem.getElectricProperties().receive.getVoltage();
				double machineVoltage = electro.getVoltage();
				if (machineVoltage > recieveVoltage) {
					electricItem.overVoltage(TransferPack.joulesVoltage(electricItem.getElectricProperties().receive.getJoules(), machineVoltage));
					level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
					level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2f, BlockInteraction.DESTROY);
				} else if (machineVoltage == recieveVoltage) {
					//electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored(), machineVoltage), false);
					electro.joules(electro.getJoulesStored() - electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored(), machineVoltage), false).getJoules());
				} else {
					float underVoltRatio = (float) ((float) machineVoltage / recieveVoltage);
					float itemStoredRatio = (float) ((float) electricItem.getJoulesStored(itemInput) / electricItem.getElectricProperties().capacity);

					float x = Math.abs(itemStoredRatio / (itemStoredRatio - underVoltRatio + 0.00000001F/* ensures it's never zero */));
					float reductionCoef = getRationalFunctionValue(x);
					if (itemStoredRatio >= underVoltRatio) {
						electricItem.extractPower(itemInput, electro.getJoulesStored() * reductionCoef, false);
					} else {
						//electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored() * reductionCoef, recieveVoltage), false);
						electro.joules(electro.getJoulesStored() - electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored() * reductionCoef, recieveVoltage), false).getJoules());
					}
				}
				if (electricItem.getJoulesStored(itemInput) == electricItem.getElectricProperties().capacity && inventory.getItem(4).isEmpty()) {
					inventory.setItem(4, inventory.getItem(0).copy());
					inventory.getItem(0).shrink(1);
				}
				this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
			}
		}

	}

	// to simulate undervolting a chargeable object
	private static float getRationalFunctionValue(float x) {
		if (x >= 100.0F) {
			return 0.0F;
		} else if (x <= 1.0F) {
			return 1.0F;
		} else {
			return 1 / x;
		}
	}
	
	private boolean drainBatterySlots(ComponentInventory inv, ComponentElectrodynamic electro) {
		double machineVoltage = electro.getVoltage();
		double battVoltage = 0;
		for(int i = 0; i < BATTERY_COUNT; i++) {
			ItemStack battery = inv.getItem(i + 1);
			if(!battery.isEmpty() && battery.getItem() instanceof IItemElectric electricItem) {
				battVoltage = electricItem.getElectricProperties().receive.getVoltage();
				if(battVoltage < machineVoltage) {
					inv.setItem(i + 1, new ItemStack(DeferredRegisters.SLAG.get()).copy());
					getLevel().playSound(null, getBlockPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1F, 1F);
				} else if (battVoltage > machineVoltage) {
					electro.overVoltage(TransferPack.joulesVoltage(electro.getJoulesStored(), battVoltage));
					return true;
				} else if (electro.getMaxJoulesStored() - electro.getJoulesStored() > 0) {
					electro.joules(electro.getJoulesStored() + electricItem.extractPower(battery, MAX_BATTERY_TRANSFER_JOULES, false).getJoules());				}
				//Electrodynamics.LOGGER.info(electro.getJoulesStored());
			}
		}
		return false;
	}

	protected void loadFromNBT(CompoundTag nbt) {
		NonNullList<ItemStack> obj = this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems();
		obj.clear();
		ContainerHelper.loadAllItems(nbt, obj);
	}

	protected void saveToNBT(CompoundTag nbt) {
		ContainerHelper.saveAllItems(nbt, this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems());
	}

}
