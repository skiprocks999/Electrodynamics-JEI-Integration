package electrodynamics.prefab.screen.component.types.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentGeneric;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ScreenComponentInventoryIO extends ScreenComponentGeneric {

	public static final int SQUARE_SIZE = 6;
	public static final int COLORS_PER_ROW = 4;
	public static final int DRAWING_AREA_SIZE = 24;

	private final Direction side;

	public ScreenComponentInventoryIO(int x, int y, Direction side) {
		super(InventoryIOTextures.DEFAULT, x, y);
		this.side = side;
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);

		GenericScreen<?> screen = (GenericScreen<?>) gui;

		GenericContainerBlockEntity<?> container = (GenericContainerBlockEntity<?>) screen.getMenu();

		GenericTile tile = (GenericTile) container.getHostFromIntArray();

		if (tile == null || !tile.hasComponent(IComponentType.Inventory)) {
			return;
		}

		ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

		HashSet<Integer> slots = inv.relativeDirectionToSlotsMap[side.ordinal()];

		if (slots == null) {
			return;
		}

		List<Color> uniqueColors = new ArrayList<>();

		slots.forEach(slot -> {
			SlotGeneric generic = (SlotGeneric) container.slots.get(slot);

			if (generic.ioColor == null) {
				return;
			}
			for (Color col : uniqueColors) {
				if (col.equals(generic.ioColor)) {
					return;
				}
			}
			uniqueColors.add(generic.ioColor);
		});

		if (uniqueColors.isEmpty()) {
			return;
		}

		if (uniqueColors.size() <= 16) {
			fillSquare(graphics, uniqueColors, xLocation + guiWidth, yLocation + guiHeight);
			return;
		}

		throw new UnsupportedOperationException("There are more than 16 unique colors mapped to a face!");

		// TODO more than 16 unique colors mapped to a single face
		// I will deal with this when it becomes a problem 10/15/23

	}

	@Override
	public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		if(isVisible() && isHovered()) {
			graphics.renderTooltip(gui.getFontRenderer(), getLabelFromDir(), xAxis, yAxis);
		}
	}

	private void fillSquare(GuiGraphics graphics, List<Color> colors, int x, int y) {
		int wholeRows = colors.size() / COLORS_PER_ROW;
		int lastRowCount = colors.size() % COLORS_PER_ROW;

		int wholeHeight = DRAWING_AREA_SIZE / (wholeRows + 1);

		int remainderHeight = DRAWING_AREA_SIZE - wholeHeight * wholeRows;

		int index = 0;

		for (int i = 0; i < wholeRows; i++) {
			for (int j = 0; j < COLORS_PER_ROW; j++) {

				graphics.fill(x + 1 + j * SQUARE_SIZE, y + 1 + i * wholeHeight, x + 1 + (j + 1) * SQUARE_SIZE, y + 1 + (i + 1) * wholeHeight, colors.get(index).color());

				index++;
			}
		}

		int remainderWidth = DRAWING_AREA_SIZE / lastRowCount;

		int lastToAdd = DRAWING_AREA_SIZE - remainderWidth * lastRowCount;

		for (int i = 0; i < lastRowCount - 1; i++) {

			graphics.fill(x + 1 + i * remainderWidth, y + 1 + wholeHeight * wholeRows, x + 1 + (i + 1) * remainderWidth, y + 1 + wholeHeight * wholeRows + remainderHeight, colors.get(index).color());

			index++;
		}

		graphics.fill(x + 1 + remainderWidth * (lastRowCount - 1), y + 1 + wholeHeight * wholeRows, x + 1 + lastRowCount * remainderWidth + lastToAdd, y + 1 + wholeHeight * wholeRows + remainderHeight, colors.get(index).color());

	}

	private MutableComponent getLabelFromDir() {
		return switch (side) {
		case DOWN -> ElectroTextUtils.tooltip("inventoryio.bottom");
		case UP -> ElectroTextUtils.tooltip("inventoryio.top");
		case EAST -> ElectroTextUtils.tooltip("inventoryio.left");
		case WEST -> ElectroTextUtils.tooltip("inventoryio.right");
		case NORTH -> ElectroTextUtils.tooltip("inventoryio.front");
		case SOUTH -> ElectroTextUtils.tooltip("inventoryio.back");
		};
	}

	public enum InventoryIOTextures implements ITexture {
		DEFAULT(26, 26, 0, 0, 26, 26, "slotio");

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		InventoryIOTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.loc = new ResourceLocation(References.ID + ":textures/screen/component/io/" + name + ".png");
		}

		@Override
		public ResourceLocation getLocation() {
			return loc;
		}

		@Override
		public int imageHeight() {
			return imageHeight;
		}

		@Override
		public int imageWidth() {
			return imageWidth;
		}

		@Override
		public int textureHeight() {
			return textureHeight;
		}

		@Override
		public int textureU() {
			return textureU;
		}

		@Override
		public int textureV() {
			return textureV;
		}

		@Override
		public int textureWidth() {
			return textureWidth;
		}

	}

}
