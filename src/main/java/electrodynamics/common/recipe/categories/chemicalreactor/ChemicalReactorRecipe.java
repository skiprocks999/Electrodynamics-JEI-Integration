package electrodynamics.common.recipe.categories.chemicalreactor;

import com.mojang.datafixers.util.Pair;
import electrodynamics.api.References;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.recipeutils.*;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ChemicalReactorRecipe extends AbstractMaterialRecipe {

    public static final String RECIPE_GROUP = "chemical_reactor_recipe";
    public static final ResourceLocation RECIPE_ID = ResourceLocation.fromNamespaceAndPath(References.ID, RECIPE_GROUP);

    private List<CountableIngredient> itemIngredients;
    private List<FluidIngredient> fluidIngredients;
    private List<GasIngredient> gasIngredients;
    private ItemStack itemOutput;
    private FluidStack fluidOutput;
    private GasStack gasOutput;

    public ChemicalReactorRecipe(String recipeGroup, List<CountableIngredient> inputItems, List<FluidIngredient> inputFluids, List<GasIngredient> inputGases, ItemStack itemOutput, FluidStack fluidOutput, GasStack gasOutput, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(recipeGroup, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
        if (inputItems.size() == 0 && inputGases.size() == 0 && inputFluids.size() == 0) {
            throw new RuntimeException("Yoou have created a chemical reactor recipe with no inputs");
        }
        if (itemOutput.isEmpty() && fluidOutput.isEmpty() && gasOutput.isEmpty()) {
            throw new RuntimeException("You have created a chemical reactor recipe with no outputs");
        }
        this.itemIngredients = inputItems;
        this.fluidIngredients = inputFluids;
        this.gasIngredients = inputGases;
        this.itemOutput = itemOutput;
        this.fluidOutput = fluidOutput;
        this.gasOutput = gasOutput;

    }

    @Override
    public boolean matchesRecipe(ComponentProcessor pr) {

        boolean flag = false;

        if (hasItemInputs()) {
            Pair<List<Integer>, Boolean> itemPair = areItemsValid(getCountedIngredients(), ((ComponentInventory) pr.getHolder().getComponent(IComponentType.Inventory)).getInputsForProcessor(pr.getProcessorNumber()));
            if (itemPair.getSecond()) {
                setItemArrangement(pr.getProcessorNumber(), itemPair.getFirst());
                flag = true;
            }
        }

        if (hasFluidInputs()) {
            Pair<List<Integer>, Boolean> fluidPair = areFluidsValid(getFluidIngredients(), pr.getHolder().<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks());
            if (fluidPair.getSecond()) {
                setFluidArrangement(fluidPair.getFirst());
                flag = true;
            }
        }

        if (hasGasInputs()) {
            Pair<List<Integer>, Boolean> gasPair = areGasesValid(getGasIngredients(), pr.getHolder().<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks());
            if (gasPair.getSecond()) {
                setGasArrangement(gasPair.getFirst());
                flag = true;
            }
        }

        return flag;
    }

    public boolean hasItemInputs() {
        return getCountedIngredients().size() > 0;
    }

    public boolean hasFluidInputs() {
        return getFluidIngredients().size() > 0;
    }

    public boolean hasGasInputs() {
        return getGasIngredients().size() > 0;
    }

    public List<CountableIngredient> getCountedIngredients() {
        return itemIngredients;
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public List<GasIngredient> getGasIngredients() {
        return gasIngredients;
    }

    public boolean hasItemOutput() {
        return !getItemRecipeOutput().isEmpty();
    }

    public boolean hasFluidOutput() {
        return !getFluidRecipeOutput().isEmpty();
    }

    public boolean hasGasOutput() {
        return !getGasRecipeOutput().isEmpty();
    }

    @Override
    public ItemStack getItemRecipeOutput() {
        return itemOutput;
    }

    @Override
    public FluidStack getFluidRecipeOutput() {
        return fluidOutput;
    }

    @Override
    public GasStack getGasRecipeOutput() {
        return gasOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get();
    }
}
