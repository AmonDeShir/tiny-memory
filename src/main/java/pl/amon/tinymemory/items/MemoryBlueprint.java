package pl.amon.tinymemory.items;


import net.minecraft.world.item.Item;
import pl.amon.tinymemory.setup.ModSetup;

public class MemoryBlueprint extends Item {

  public MemoryBlueprint() {
    super(new Item.Properties().tab(ModSetup.ITEM_GROUP));
  }
  
}
