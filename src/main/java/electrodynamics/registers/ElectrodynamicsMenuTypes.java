package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.item.ContainerElectricDrill;
import electrodynamics.common.inventory.container.item.ContainerGuidebook;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.inventory.container.tile.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, References.ID);

	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCoalGenerator>> CONTAINER_COALGENERATOR = register(SubtypeMachine.coalgenerator.tag(), ContainerCoalGenerator::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricFurnace>> CONTAINER_ELECTRICFURNACE = register(SubtypeMachine.electricfurnace.tag(), ContainerElectricFurnace::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricFurnaceDouble>> CONTAINER_ELECTRICFURNACEDOUBLE = register(SubtypeMachine.electricfurnacedouble.tag(), ContainerElectricFurnaceDouble::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricFurnaceTriple>> CONTAINER_ELECTRICFURNACETRIPLE = register(SubtypeMachine.electricfurnacetriple.tag(), ContainerElectricFurnaceTriple::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricArcFurnace>> CONTAINER_ELECTRICARCFURNACE = register(SubtypeMachine.electricarcfurnace.tag(), ContainerElectricArcFurnace::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricArcFurnaceDouble>> CONTAINER_ELECTRICARCFURNACEDOUBLE = register(SubtypeMachine.electricarcfurnacedouble.tag(), ContainerElectricArcFurnaceDouble::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricArcFurnaceTriple>> CONTAINER_ELECTRICARCFURNACETRIPLE = register(SubtypeMachine.electricarcfurnacetriple.tag(), ContainerElectricArcFurnaceTriple::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerO2OProcessor>> CONTAINER_O2OPROCESSOR = register("o2oprocessor", ContainerO2OProcessor::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerO2OProcessorDouble>> CONTAINER_O2OPROCESSORDOUBLE = register("o2oprocessordouble", ContainerO2OProcessorDouble::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerO2OProcessorTriple>> CONTAINER_O2OPROCESSORTRIPLE = register("o2oprocessortriple", ContainerO2OProcessorTriple::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerDO2OProcessor>> CONTAINER_DO2OPROCESSOR = register("do2oprocessor", ContainerDO2OProcessor::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerBatteryBox>> CONTAINER_BATTERYBOX = register(SubtypeMachine.batterybox.tag(), ContainerBatteryBox::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFermentationPlant>> CONTAINER_FERMENTATIONPLANT = register(SubtypeMachine.fermentationplant.tag(), ContainerFermentationPlant::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerMineralWasher>> CONTAINER_MINERALWASHER = register(SubtypeMachine.mineralwasher.tag(), ContainerMineralWasher::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerChemicalMixer>> CONTAINER_CHEMICALMIXER = register(SubtypeMachine.chemicalmixer.tag(), ContainerChemicalMixer::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerChemicalCrystallizer>> CONTAINER_CHEMICALCRYSTALLIZER = register(SubtypeMachine.chemicalcrystallizer.tag(), ContainerChemicalCrystallizer::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerChargerGeneric>> CONTAINER_CHARGER = register("genericcharger", ContainerChargerGeneric::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFluidTankGeneric>> CONTAINER_TANK = register("generictank", ContainerFluidTankGeneric::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCombustionChamber>> CONTAINER_COMBUSTION_CHAMBER = register("combustionchamber", ContainerCombustionChamber::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerSolarPanel>> CONTAINER_SOLARPANEL = register("solarpanel", ContainerSolarPanel::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerWindmill>> CONTAINER_WINDMILL = register("windmill", ContainerWindmill::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerHydroelectricGenerator>> CONTAINER_HYDROELECTRICGENERATOR = register("hydroelectricgenerator", ContainerHydroelectricGenerator::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCreativePowerSource>> CONTAINER_CREATIVEPOWERSOURCE = register("creativepowersource", ContainerCreativePowerSource::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCreativeFluidSource>> CONTAINER_CREATIVEFLUIDSOURCE = register("creativefluidsource", ContainerCreativeFluidSource::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFluidVoid>> CONTAINER_FLUIDVOID = register("fluidvoid", ContainerFluidVoid::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerSeismicScanner>> CONTAINER_SEISMICSCANNER = register("seismicdetector", ContainerSeismicScanner::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectrolyticSeparator>> CONTAINER_ELECTROLYTICSEPARATOR = register("electrolyticseparator", ContainerElectrolyticSeparator::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerSeismicRelay>> CONTAINER_SEISMICRELAY = register("seismicrelay", ContainerSeismicRelay::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCoolantResavoir>> CONTAINER_COOLANTRESAVOIR = register("coolantresavoir", ContainerCoolantResavoir::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerMotorComplex>> CONTAINER_MOTORCOMPLEX = register("motorcomplex", ContainerMotorComplex::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerQuarry>> CONTAINER_QUARRY = register("quarry", ContainerQuarry::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerGuidebook>> CONTAINER_GUIDEBOOK = register("guidebook", ContainerGuidebook::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerGasTankGeneric>> CONTAINER_GASTANK = register("gastank", ContainerGasTankGeneric::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCompressor>> CONTAINER_COMPRESSOR = register("compressor", ContainerCompressor::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerDecompressor>> CONTAINER_DECOMPRESSOR = register("decompressor", ContainerDecompressor::new);

	public static final DeferredHolder<MenuType<?>,MenuType<ContainerAdvancedCompressor>> CONTAINER_ADVANCEDCOMPRESSOR = register("advancedcompressor", ContainerAdvancedCompressor::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerAdvancedDecompressor>> CONTAINER_ADVANCEDDECOMPRESSOR = register("advanceddecompressor", ContainerAdvancedDecompressor::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerGasVent>> CONTAINER_GASVENT = register("gasvent", ContainerGasVent::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerThermoelectricManipulator>> CONTAINER_THERMOELECTRICMANIPULATOR = register("thermoelectricmanipulator", ContainerThermoelectricManipulator::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerGasPipePump>> CONTAINER_GASPIPEPUMP = register("gaspipepump", ContainerGasPipePump::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFluidPipePump>> CONTAINER_FLUIDPIPEPUMP = register("fluidpipepump", ContainerFluidPipePump::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerGasPipeFilter>> CONTAINER_GASPIPEFILTER = register("gaspipefilter", ContainerGasPipeFilter::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerFluidPipeFilter>> CONTAINER_FLUIDPIPEFILTER = register("fluidpipefilter", ContainerFluidPipeFilter::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerElectricDrill>> CONTAINER_ELECTRICDRILL = register("electricdrill", ContainerElectricDrill::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerPotentiometer>> CONTAINER_POTENTIOMETER = register("potentiometer", ContainerPotentiometer::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerAdvancedUpgradeTransformer>> CONTAINER_ADVANCEDUPGRADETRANSFORMER = register("advancedupgradetransformer", ContainerAdvancedUpgradeTransformer::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerAdvancedDowngradeTransformer>> CONTAINER_ADVANCEDDOWNGRADETRANSFORMER = register("advanceddowngradetransformer", ContainerAdvancedDowngradeTransformer::new);
	public static final DeferredHolder<MenuType<?>,MenuType<ContainerCircuitMonitor>> CONTAINER_CIRCUITMONITOR = register("circuitmonitor", ContainerCircuitMonitor::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ContainerGasCollector>> CONTAINER_GASCOLLECTOR = register("gascollector", ContainerGasCollector::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ContainerChemicalReactor>> CONTAINER_CHEMICALREACTOR = register("chemicalreactor", ContainerChemicalReactor::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ContainerCreativeGasSource>> CONTAINER_CREATIVEGASSOURCE = register("creativegassource", ContainerCreativeGasSource::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ContainerElectrolosisChamber>> CONTAINER_ELECTROLOSISCHAMBER = register("electrolosischamber", ContainerElectrolosisChamber::new);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>,MenuType<T>> register(String id, MenuSupplier<T> supplier) {
		return MENU_TYPES.register(id, () -> new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS));
	}
}
