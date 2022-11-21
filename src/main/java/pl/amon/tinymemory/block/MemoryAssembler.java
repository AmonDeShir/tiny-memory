package pl.amon.tinymemory.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class MemoryAssembler extends Block {
  public MemoryAssembler() {
    super(Block.Properties.of(Material.METAL).strength(2f));
  }
}
