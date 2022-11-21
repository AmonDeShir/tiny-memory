package pl.amon.tinymemory.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import pl.amon.tinymemory.TinyMemory;

public class ToolTippedBlockItem extends BlockItem {
  protected String blockId;

  public ToolTippedBlockItem(Block block, ResourceLocation blockId, Properties properties) {
    super(block, properties);
    this.blockId = blockId.toString().replace(":", ".");
  }

  @Override
  public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag flag) {
    if (Screen.hasShiftDown()) {
      components.add(Component.translatable(String.format("message.block.%s", blockId)).withStyle(ChatFormatting.AQUA));
    }
    else {
      components.add(Component.translatable(String.format("message.%s.press_shift_for_more_info", TinyMemory.MODID)).withStyle(ChatFormatting.YELLOW));
    }

    super.appendHoverText(item, level, components, flag);
  }
}
