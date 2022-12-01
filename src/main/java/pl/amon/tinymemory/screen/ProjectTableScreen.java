package pl.amon.tinymemory.screen;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import pl.amon.tinymemory.TinyMemory;

public class ProjectTableScreen extends AbstractContainerScreen<ProjectTableMenu>{
  private static final ResourceLocation TEXTURE = new ResourceLocation(TinyMemory.MODID, "textures/gui/project_table.png");
  private IOPort[] ports = new IOPort[32];

  public ProjectTableScreen(ProjectTableMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
    
    this.leftPos = 0;
    this.topPos = 0;
    this.imageWidth = 193;
    this.imageHeight = 219;
  }

  @Override
  protected void init() {
    super.init();

    for (int i = 0; i < ports.length; i++) {
      ports[i] = new IOPort(i, this.leftPos, this.topPos);
      ports[i].value = i % 2;
      this.addWidget(ports[i].button);
    }
  }

  @Override
  protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
    renderBackground(stack);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    
    blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    for (int i = 0; i < ports.length; i++) {
      ports[i].draw(stack);
      ports[i].hover(stack, mouseX, mouseY);
    }
  }

  @Override
  public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
    super.render(stack, mouseX, mouseY, partialTicks);

    this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 15, this.topPos + 126, 0x404040);
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
    public Button button;

    public IOPort (int index, int leftPos, int topPos) {
      this.side = Math.floorDiv(index, 8);
      this.index = index % 8;

      pivotFromSide(leftPos, topPos);

      this.button = new Button(textX, textY, 3, 5, Component.empty(), this::onPress);
    }
    
    public void onPress(Button button) {
      this.value = this.value == 1 ? 0 : 1;
    }

    private void pivotFromSide(int leftPos, int topPos) {
      int m = index * 9;
  
      switch (side) {
        case 0 -> setPivot(64 + m, 23, 63 + m, 15);
        case 1 -> setPivot(132, 32 + m, 140, 30 + m);
        case 2 -> setPivot(127 - m, 100, 126 - m, 108);
        case 3 -> setPivot(55, 95 - m, 49, 93 - m);
        default -> setPivot(64 + m, 25, 63 + m, 15);
      };

      this.redstoneX += leftPos;
      this.redstoneY += topPos;
      this.textX += leftPos;
      this.textY += topPos;
    }

    private void setPivot(int rx, int ry, int tx, int ty) {
      redstoneX = rx;
      redstoneY = ry;
      textX = tx;
      textY = ty;
    }

    public void draw(PoseStack stack) {
      renderRedstone(stack);
      renderText(stack);
    }

    private void renderRedstone(PoseStack stack) {
      int enabled = 2 * this.value;

      if (this.side == 0 || this.side == 2) {
        blit(stack, redstoneX, redstoneY, 64 - enabled, 220, 1, 5);
      }
      else {
        blit(stack, redstoneX, redstoneY, 56, 222 - enabled, 5, 1);
      }
    }

    private void renderText(PoseStack stack) {
      int enabled = 4 * this.value;
      blit(stack, textX, textY, 16 + enabled, 220, 3, 5);
    }

    public void hover(PoseStack stack, int mouseX, int mouseY) {
      int enabled = 4 * this.value;

      int sizeX = 3;
      int sizeY = 5;

      if ((mouseX >= textX && mouseX <= textX + sizeX) && (mouseY >= textY && mouseY <= textY + sizeY)) {
        blit(stack, textX, textY, 16 + enabled, 226, 3, 5);
      }
    }
  }
}
