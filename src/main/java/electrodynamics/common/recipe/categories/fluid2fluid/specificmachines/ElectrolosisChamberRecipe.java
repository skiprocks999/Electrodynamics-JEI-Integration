package electrodynamics.common.recipe.categories.fluid2fluid.specificmachines;

import electrodynamics.Electrodynamics;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.fluid2fluid.Fluid2FluidRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ElectrolosisChamberRecipe extends Fluid2FluidRecipe {

    public static final String RECIPE_GROUP = "electrolosis_chamber_recipe";
    public static final ResourceLocation RECIPE_ID = Electrodynamics.rl(RECIPE_GROUP);
    public ElectrolosisChamberRecipe(String recipeGroup, List<FluidIngredient> inputFluids, FluidStack outputFluid, double experience, int ticks, double usagePerTick, List<ProbableItem> itemBiproducts, List<ProbableFluid> fluidBiproducts, List<ProbableGas> gasBiproducts) {
        super(recipeGroup, inputFluids, outputFluid, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_TYPE.get();
    }
}
