package pl.amon.tinymemory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.screen.components.utils.Drawer;

public class ProjectTableScreen extends AbstractContainerScreen<ProjectTableMenu>{
  private static final ResourceLocation TEXTURE = new ResourceLocation(TinyMemory.MODID, "textures/gui/project_table.png");
  private Drawer drawer;

  public ProjectTableScreen(ProjectTableMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
    
    this.leftPos = 0;
    this.topPos = 0;
    this.imageWidth = 193;
    this.imageHeight = 219;

    this.drawer = Drawer.builder()
      .numbers(16, 220)
      .verticalLine(0, 2)
      .horizontalLine(2, 0)
      .bigArrow(66, 220)
      .minus(4, 226)
      .plus(8, 226)
      .arrowHeadDown(8, 220)
      .arrowHeadUp(4, 220)
      .build(this::blit);
    }

  @Override
  protected void init() {
    super.init();

    for (int i = 0; i < this.menu.ports.length; i++) {
      this.menu.ports[i].updatePosition(leftPos, topPos);
      this.addWidget(this.menu.ports[i].button);
    }
  }

  @Override
  protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
    renderBackground(stack);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    
    blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    for (int i = 0; i < this.menu.ports.length; i++) {
      this.menu.ports[i].draw(stack, drawer);
      this.menu.ports[i].hover(stack, drawer, mouseX, mouseY);
    }

    drawer.drawBox(stack, 60 + this.leftPos, 106 + this.topPos, 44, 8);
    drawer.drawBigArrow(stack, 44 + this.leftPos, 108 + this.topPos);
  }

  @Override
  public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
    super.render(stack, mouseX, mouseY, partialTicks);

    this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 15, this.topPos + 126, 0x404040);
  }

  @Override
  protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {}
}
