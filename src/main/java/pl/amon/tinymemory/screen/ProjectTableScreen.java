package pl.amon.tinymemory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;
import pl.amon.tinymemory.TinyMemory;

public class ProjectTableScreen extends AbstractContainerScreen<ProjectTableMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation(TinyMemory.MODID, "textures/gui/project_table.png");
  private IOPort[] ports = new IOPort[32];

  public ProjectTableScreen(ProjectTableMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
    
    this.leftPos = 0;
    this.topPos = 0;
    this.imageWidth = 193;
    this.imageHeight = 219;

    for (int i = 0; i < ports.length; i++) {
      ports[i] = new IOPort(i);
    }
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
    renderBackground(stack);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    
    blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
  }


  @Override
  public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
    super.render(stack, mouseX, mouseY, partialTicks);

    this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 15, this.topPos + 126, 0x404040);

    for (int i = 0; i < ports.length; i++) {
      ports[i].draw(stack, this.leftPos, this.topPos);
      ports[i].hover();
    }
  }

  @Override
  protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {}

  private class IOPort {
    public int side;
    public int index;
    public int redstoneX;
    public int redstoneY;
    public int textX;
    public int textY;
    public int value = 0;

    public IOPort (int index) {
      this.side = Math.floorDiv(index, 8);
      this.index = index % 8;

      pivotFromSide();
    }
    
    private void pivotFromSide() {
      int m = index * 9;
  
      switch (side) {
        case 0 -> setPivot(66 + m, 24, 64 + m, 16);
        case 1 -> setPivot(131, 33 + m, 140, 31 + m);
        case 2 -> setPivot(127 - m, 99, 128 - m, 107);
        case 3 -> setPivot(56, 94 - m, 50, 92 - m);
        default -> setPivot(66 + m, 24, 64 + m, 16);
      };
    }

    private void setPivot(int rx, int ry, int tx, int ty) {
      redstoneX = rx;
      redstoneY = ry;
      textX = tx;
      textY = ty;
    }

    public void draw(PoseStack stack, int leftPos, int topPos) {
      int enabled = 2 * this.value;

      if (this.side == 0 || this.side == 2) {
        blit(stack, redstoneX + leftPos, redstoneY + topPos, 64, 220, 1, 5);
      }
      else {
        blit(stack, redstoneX + leftPos, redstoneY + topPos, 56, 222, 5, 1);
      }
    }

    public void hover() {

    }
  }
}
