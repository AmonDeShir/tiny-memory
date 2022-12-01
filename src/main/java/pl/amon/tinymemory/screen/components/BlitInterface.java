package pl.amon.tinymemory.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;

public interface BlitInterface {
  public void blit(PoseStack stack, int x, int y, int sourceX, int sourceY, int w, int h);
}
