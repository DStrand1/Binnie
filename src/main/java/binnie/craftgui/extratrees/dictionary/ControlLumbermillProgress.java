package binnie.craftgui.extratrees.dictionary;

import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.minecraft.MinecraftGUI;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlProgressBase;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.craftgui.window.Panel;
import binnie.extratrees.core.ExtraTreeTexture;
import binnie.extratrees.machines.lumbermill.LumbermillMachine;
import binnie.extratrees.machines.lumbermill.LumbermillRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ControlLumbermillProgress extends ControlProgressBase {
	float oldProgress;
	float animation;
	static Texture Saw = new StandardTexture(0, 0, 6, 32, ExtraTreeTexture.Gui);
	static Texture Saw2 = new StandardTexture(2, 0, 4, 32, ExtraTreeTexture.Gui);

	@Override
	public void onUpdateClient() {
		super.onUpdateClient();
		if (this.oldProgress != this.progress) {
			this.oldProgress = this.progress;
			this.animation += 5.0f;
		}
	}

	@Override
	public void onRenderForeground() {
		GL11.glDisable(2896);
		final int sawX = (int) (63.0f * this.progress);
		CraftGUI.render.texture(ControlLumbermillProgress.Saw, new IPoint(sawX, -8.0f + 6.0f * (float) Math.sin(this.animation)));
		final ItemStack item = Window.get(this).getInventory().getStackInSlot(LumbermillMachine.SLOT_LOG);
		if (item == null) {
			return;
		}
		GL11.glDisable(2896);
		Block block = null;
		if (item.getItem() instanceof ItemBlock) {
			block = ((ItemBlock) item.getItem()).getBlock();
		}
		if (block == null) {
			return;
		}
		//TODO RENDERING
		//final IIcon icon = block.getIcon(2, item.getItemDamage());
		for (int i = 0; i < 4; ++i) {
			//CraftGUI.Render.iconBlock(new IPoint(1 + i * 16, 1.0f), icon);
		}
		final ItemStack result = LumbermillRecipes.getPlankProduct(item);
		if (result == null) {
			return;
		}
		Block block2 = null;
		if (item.getItem() instanceof ItemBlock) {
			block2 = ((ItemBlock) result.getItem()).getBlock();
		}
		if (block2 == null) {
			return;
		}
		//final IIcon icon2 = block2.getIcon(2, result.getItemDamage());
		final IPoint size = this.getSize();
		final IPoint pos = this.getAbsolutePosition();
		CraftGUI.render.limitArea(new IArea(pos.add(new IPoint(0.0f, 0.0f)), new IPoint(this.progress * 64.0f + 2.0f, 18.0f)));
		GL11.glEnable(3089);
		for (int j = 0; j < 4; ++j) {
			//TODO RENDERING
			//CraftGUI.Render.iconBlock(new IPoint(1 + j * 16, 1.0f), icon2);
		}
		GL11.glDisable(3089);
		CraftGUI.render.texture(ControlLumbermillProgress.Saw2, new IPoint(sawX + 2, -8.0f + 6.0f * (float) Math.sin(this.animation)));
	}

	protected ControlLumbermillProgress(final IWidget parent, final float x, final float y) {
		super(parent, x, y, 66.0f, 18.0f);
		this.oldProgress = 0.0f;
		this.animation = 0.0f;
		new Panel(this, 0.0f, 0.0f, 66.0f, 18.0f, MinecraftGUI.PanelType.Black);
	}

}
