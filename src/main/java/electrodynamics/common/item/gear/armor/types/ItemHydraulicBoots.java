package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import electrodynamics.api.References;
import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.render.model.armor.types.ModelHydraulicBoots;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class ItemHydraulicBoots extends ItemElectrodynamicsArmor {

    public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(Type.HELMET, 0);
        map.put(Type.CHESTPLATE, 0);
        map.put(Type.LEGGINGS, 0);
        map.put(Type.BOOTS, 1);
    });
    public static final int MAX_CAPACITY = 2000;

    private static final String TEXTURE_LOCATION = References.ID + ":textures/model/armor/hydraulicboots.png";

    public ItemHydraulicBoots() {
        super(ElectrodynamicsArmorMaterials.HYDRAULIC_BOOTS, Type.BOOTS, new Item.Properties().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

                ModelHydraulicBoots<LivingEntity> model = new ModelHydraulicBoots<>(ClientRegister.HYDRAULIC_BOOTS.bakeRoot());

                model.crouching = properties.crouching;
                model.riding = properties.riding;
                model.young = properties.young;

                return model;
            }
        });
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {

        super.addCreativeModeItems(tab, items);

        if (Capabilities.FluidHandler.ITEM == null) {
            return;
        }

        ItemStack full = new ItemStack(this);

        IFluidHandlerItem handler = full.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return;
        }

        RestrictedFluidHandlerItemStack restricted = (RestrictedFluidHandlerItemStack) handler;

        restricted.setFluid(new FluidStack(ElectrodynamicsFluids.fluidHydraulic, MAX_CAPACITY));

        items.add(full);

    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {

        if (Capabilities.FluidHandler.ITEM == null) {

            super.appendHoverText(stack, context, tooltip, flagIn);

            return;

        }

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {

            super.appendHoverText(stack, context, tooltip, flagIn);

            return;

        }

        tooltip.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(handler.getFluidInTank(0).getAmount()), ChatFormatter.formatFluidMilibuckets(MAX_CAPACITY)).withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack1, ItemStack stack2) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return staticIsBarVisible(stack);
    }

    protected static boolean staticIsBarVisible(ItemStack stack) {

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return false;
        }

        return !handler.getFluidInTank(0).isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return staticGetBarWidth(stack);
    }

    protected static int staticGetBarWidth(ItemStack stack) {

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return 13;
        }

        return (int) (13.0 * handler.getFluidInTank(0).getAmount() / handler.getTankCapacity(0));

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static Predicate<FluidStack> getPredicate() {
        return fluid -> fluid.getFluid().builtInRegistryHolder().is(ElectrodynamicsTags.Fluids.HYDRAULIC_FLUID);
    }

    /*
    public enum HydraulicBoots implements ICustomArmor {
        HYDRAULIC_BOOTS;

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public String getName() {
            return References.ID + ":hydraulic_boots";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }

        @Override
        public int getDurabilityForType(Type pType) {
            return 100;
        }

        @Override
        public int getDefenseForType(Type pType) {
            return 1;
        }

    }
     */

}
