package physica.nuclear.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import physica.api.core.IBaseUtilities;
import physica.library.client.gui.GuiContainerBase;
import physica.library.inventory.ContainerBase;
import physica.nuclear.common.tile.TileInsertableControlRod;

@SideOnly(Side.CLIENT)
public class GuiInsertableControlRod extends GuiContainerBase<TileInsertableControlRod> implements IBaseUtilities {

	public GuiInsertableControlRod(EntityPlayer player, TileInsertableControlRod host) {
		super(new ContainerBase<TileInsertableControlRod>(player, host), host);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawString("Insertion: " + host.getInsertion() + "%", 50, 59);
	}

	public void initGui() {
		super.initGui();
		addButton(new GuiButton(1, width / 2 - 70, height / 2, "Raise 5%".length() * 8, 20, "Raise 5%"));
		addButton(new GuiButton(2, width / 2 - 70 + "Raise 5%".length() * 8 + 10, height / 2, "Lower 5%".length() * 8, 20, "Lower 5%"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		host.actionPerformed(button.id == 1 ? -5 : 5, Side.CLIENT);
	}
}
