package pl.amon.tinymemory.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import pl.amon.tinymemory.TinyMemory;

@Mod.EventBusSubscriber(modid = TinyMemory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("tinyredstone") {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(Registration.MEMORY_BLUEPRINT_ITEM.get());
    }
  };

  public static void init(final FMLCommonSetupEvent event) {}
}
