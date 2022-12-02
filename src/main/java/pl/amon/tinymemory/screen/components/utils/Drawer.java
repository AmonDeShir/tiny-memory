package pl.amon.tinymemory.screen.components.utils;

import com.mojang.blaze3d.vertex.PoseStack;

public class Drawer {
  private Vector numbers = null;
  private Vector bigArrow = null;
  private Vector horizontalLine = null;
  private Vector verticalLine = null;
  private Vector minus = null;
  private Vector plus = null;
  private Vector arrowHeadUp = null;
  private Vector arrowHeadDown = null;
  private BlitInterface blintProvider;

  private Drawer() {};

  private static class Vector {
    public int x = 0;
    public int y = 0;
  
    public Vector(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
  
  public void drawVerticalLine(PoseStack stack, int x, int y, int width) {
    if (blintProvider != null && verticalLine != null) {
      blintProvider.blit(stack, x, y, verticalLine.x, verticalLine.y, 1, width);
    }
  }

  public void drawHorizontalLine(PoseStack stack, int x, int y, int width) {
    if (blintProvider != null && horizontalLine != null) {
      blintProvider.blit(stack, x, y, horizontalLine.x, horizontalLine.y, width, 1);
    }
  }

  public void drawBigArrow(PoseStack stack, int x, int y) {
    if (blintProvider != null && bigArrow != null) {
      blintProvider.blit(stack, x, y, bigArrow.x, bigArrow.y, 15, 5);
    }
  }

  public void drawNumber(PoseStack stack, int x, int y, int number, boolean green) {
    if (blintProvider != null && numbers != null) {
      blintProvider.blit(stack, x, y, numbers.x + 4 * number, numbers.y + (green ? 6 : 0), 3, 5);
    }
  }

  public void drawNumber(PoseStack stack,  int x, int y, int number) {
    drawNumber(stack, x, y, number, false);
  }

  public void drawBox(PoseStack stack,  int x, int y, int width, int height) {
    drawHorizontalLine(stack, x+1, y, width-1);
    drawVerticalLine(stack, x, y+1, height-1);
    drawHorizontalLine(stack, x+1, y + height, width-1);
    drawVerticalLine(stack, x + width, y+1, height-1);
  }

  public void blit(PoseStack stack, int x, int y, int sourceX, int sourceY, int w, int h) {
    if (this.blintProvider != null) {
      this.blintProvider.blit(stack, x, y, sourceX, sourceY, w, h);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Drawer drawer = new Drawer();

    public Builder numbers(int x, int y) {
      drawer.numbers = new Vector(x, y);
      return this;
    }

    public Builder bigArrow(int x, int y) {
      drawer.bigArrow = new Vector(x, y);
      return this;
    }

    public Builder horizontalLine(int x, int y) {
      drawer.horizontalLine = new Vector(x, y);
      return this;
    }

    public Builder verticalLine(int x, int y) {
      drawer.verticalLine = new Vector(x, y);
      return this;
    }

    public Builder minus(int x, int y) {
      drawer.minus = new Vector(x, y);
      return this;
    }

    public Builder plus(int x, int y) {
      drawer.plus = new Vector(x, y);
      return this;
    }

    public Builder arrowHeadUp(int x, int y) {
      drawer.arrowHeadUp = new Vector(x, y);
      return this;
    }

    public Builder arrowHeadDown(int x, int y) {
      drawer.arrowHeadDown = new Vector(x, y);
      return this;
    }
    
    public Drawer build(BlitInterface blint) {
      drawer.blintProvider = blint;

      return this.drawer;
    }
  }
}