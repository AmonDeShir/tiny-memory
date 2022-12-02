package pl.amon.tinymemory.screen.components;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import pl.amon.tinymemory.setup.Registration;

public class BlueprintSlot extends SlotItemHandler {

  public BlueprintSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(ItemStack item) {
    return item.is(Registration.MEMORY_BLUEPRINT_ITEM.get());
  }
}