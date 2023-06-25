package electrodynamics.compatibility.jei.recipecategories.item2item.specificmachines;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralCrusherRecipe;
import electrodynamics.compatibility.jei.ElectrodynamicsJEIPlugin;
import electrodynamics.compatibility.jei.recipecategories.item2item.Item2ItemRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.types.ArrowAnimatedObject;
import electrodynamics.compatibility.jei.utils.gui.types.BackgroundObject;
import electrodynamics.compatibility.jei.utils.gui.types.ItemSlotObject;
import electrodynamics.compatibility.jei.utils.label.types.BiproductPercentWrapperElectroRecipe;
import electrodynamics.compatibility.jei.utils.label.types.PowerLabelWrapperElectroRecipe;
import electrodynamics.compatibility.jei.utils.label.types.TimeLabelWrapperElectroRecipe;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressBars;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsBlocks;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

public class MineralCrusherRecipeCategory extends Item2ItemRecipeCategory<MineralCrusherRecipe> {

	private static final BackgroundObject BACK_WRAP = new BackgroundObject(132, 58);

	private static final ItemSlotObject INPUT_SLOT = new ItemSlotObject(SlotType.NORMAL, 17, 20, RecipeIngredientRole.INPUT);
	private static final ItemSlotObject OUTPUT_SLOT = new ItemSlotObject(SlotType.BIG, 69, 16, RecipeIngredientRole.OUTPUT);
	private static final ItemSlotObject BIPRODUCT_SLOT = new ItemSlotObject(SlotType.NORMAL, 100, 20, RecipeIngredientRole.OUTPUT);

	private static final ArrowAnimatedObject ANIM_ARROW = new ArrowAnimatedObject(ProgressBars.PROGRESS_ARROW_RIGHT, 41, 23, StartDirection.LEFT);

	private static final PowerLabelWrapperElectroRecipe POWER_LABEL = new PowerLabelWrapperElectroRecipe(2, 48, 240);
	private static final BiproductPercentWrapperElectroRecipe ITEM_LABEL = new BiproductPercentWrapperElectroRecipe(100, 40, false);
	private static final TimeLabelWrapperElectroRecipe TIME_LABEL = new TimeLabelWrapperElectroRecipe(130, 48);

	private static final int ANIM_TIME = 50;

	private static final String RECIPE_GROUP = SubtypeMachine.mineralcrusher.tag();

	public static ItemStack INPUT_MACHINE = new ItemStack(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusher));

	public static final RecipeType<MineralCrusherRecipe> RECIPE_TYPE = RecipeType.create(References.ID, MineralCrusherRecipe.RECIPE_GROUP, MineralCrusherRecipe.class);

	public MineralCrusherRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, RECIPE_GROUP, INPUT_MACHINE, BACK_WRAP, RECIPE_TYPE, ANIM_TIME);
		ElectrodynamicsJEIPlugin.addO2OClickArea(RECIPE_TYPE);
		setInputSlots(guiHelper, INPUT_SLOT);
		setOutputSlots(guiHelper, OUTPUT_SLOT, BIPRODUCT_SLOT);
		setAnimatedArrows(guiHelper, ANIM_ARROW);
		setLabels(POWER_LABEL, ITEM_LABEL, TIME_LABEL);
	}

}
