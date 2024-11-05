package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.fluid.subtype.SubtypePureMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static electrodynamics.registers.ElectrodynamicsFluids.*;

public class ElectrodynamicsFluidTypes {
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, References.ID);

	// liquids
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_CLAY = FLUID_TYPES.register("fluidclay", () -> FLUID_CLAY.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_ETHANOL = FLUID_TYPES.register("fluidethanol", () -> FLUID_ETHANOL.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_HYDRAULIC = FLUID_TYPES.register("fluidhydraulic", () -> FLUID_HYDRAULIC.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_HYDROFLUORICACID = FLUID_TYPES.register("fluidhydrofluoricacid", () -> FLUID_HYDROFLUORICACID.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_HYDROGEN = FLUID_TYPES.register("fluidhydrogen", () -> FLUID_HYDROGEN.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_OXYGEN = FLUID_TYPES.register("fluidoxygen", () -> FLUID_OXYGEN.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_POLYETHYLENE = FLUID_TYPES.register("fluidpolyethylene", () -> FLUID_POLYETHYLENE.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_SULFURICACID = FLUID_TYPES.register("fluidsulfuricacid", () -> FLUID_SULFURICACID.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_NITRICACID = FLUID_TYPES.register("fluidnitricacid", () -> FLUID_NITRICACID.get().getFluidType());
	public static final DeferredHolder<FluidType, FluidType> FLUID_TYPE_HYDROCHLORICACID = FLUID_TYPES.register("fluidhydrochloricacid", () -> FLUID_HYDROCHLORICACID.get().getFluidType());

	public static final BulkDeferredHolder<FluidType, FluidType, SubtypeSulfateFluid> FLUID_TYPES_SULFATE = new BulkDeferredHolder<>(SubtypeSulfateFluid.values(), subtype -> FLUID_TYPES.register("fluidsulfate" + subtype.tag(), () -> ElectrodynamicsFluids.FLUIDS_SULFATE.getValue(subtype).getFluidType()));
	public static final BulkDeferredHolder<FluidType, FluidType, SubtypePureMineralFluid> FLUID_TYPES_PUREMINERAL = new BulkDeferredHolder<>(SubtypePureMineralFluid.values(), subtype -> FLUID_TYPES.register("fluidpuremineral" + subtype.tag(), () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(subtype).getFluidType()));

}
