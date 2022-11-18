package pl.amon.tinymemory.elements;

import com.dannyandson.tinygates.gates.AbstractGate;
import com.dannyandson.tinyredstone.blocks.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class ROM extends AbstractGate {
 
  @Override
  public void render(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, float alpha) {
  
  }

  @Override
  public boolean neighborChanged(PanelCellPos cellPos) {
    PanelCellNeighbor right = cellPos.getNeighbor(Side.RIGHT), left = cellPos.getNeighbor(Side.LEFT);

    boolean output = right == null || right.getWeakRsOutput() <= 0 || left == null || left.getWeakRsOutput() <= 0;

    if (output != this.output) {
      this.output = output;
      return true;
    }

    return false;
  }
}