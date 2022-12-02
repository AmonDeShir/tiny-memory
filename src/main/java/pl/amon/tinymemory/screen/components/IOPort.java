package pl.amon.tinymemory.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.screen.components.utils.Drawer;

public class IOPort {
  public int side;
  public int index;
  public int redstoneX;
  public int redstoneY;
  public int textX;
  public int textY;
  public int value = 0;
  public Button button;
  public IOPortUpdate onUpdate;

  public IOPort (int index) {
    this.side = Math.floorDiv(index, 8);
    this.index = index % 8;
    this.button = new Button(0, 0, 3, 5, Component.empty(), this::onPress);

    pivotFromSide(0, 0);
  }

  public void onPress(Button button) {
    this.value = this.value == 1 ? 0 : 1;
    TinyMemory.LOGGER.info(String.format("Update button IOport, value: %d", this.value));
    
    if (this.onUpdate != null) {
      this.onUpdate.onUpdate(this.value);
    }
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

    this.button.x = this.textX;
    this.button.y = this.textY;
  }

  private void setPivot(int rx, int ry, int tx, int ty) {
    redstoneX = rx;
    redstoneY = ry;
    textX = tx;
    textY = ty;
  }

  public void updatePosition(int leftPos, int topPos) {
    this.pivotFromSide(leftPos, topPos);
  }

  public void draw(PoseStack stack, Drawer drawer) {
    renderRedstone(stack, drawer);
    renderText(stack, drawer);
  }

  private void renderRedstone(PoseStack stack, Drawer drawer) {
    int enabled = 2 * this.value;

    if (this.side == 0 || this.side == 2) {
      drawer.blit(stack, redstoneX, redstoneY, 64 - enabled, 220, 1, 5);
    }
    else {
      drawer.blit(stack, redstoneX, redstoneY, 56, 222 - enabled, 5, 1);
    }
  }

  private void renderText(PoseStack stack, Drawer drawer) {
    drawer.drawNumber(stack, textX, textY, this.value);
  }

  public void hover(PoseStack stack, Drawer drawer, int mouseX, int mouseY) {
    int sizeX = 3;
    int sizeY = 5;

    if ((mouseX >= textX && mouseX < textX + sizeX) && (mouseY >= textY && mouseY < textY + sizeY)) {
      drawer.drawNumber(stack, textX, textY, this.value, true);
    }
  }

  public interface IOPortUpdate {
    void onUpdate(int value);
  }
}
