package pl.amon.tinymemory.items;


import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.setup.ModSetup;

public class MemoryBlueprint extends Item {

  public MemoryBlueprint() {
    super(new Item.Properties().tab(ModSetup.ITEM_GROUP));
  }
 
  @Override
  public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag flag) {
    components.add(Component.translatable(String.format("message.item.%s.memory_blueprint", TinyMemory.MODID)).withStyle(ChatFormatting.AQUA));
    super.appendHoverText(item, level, components, flag);
  }
}
