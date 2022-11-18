package pl.amon.tinymemory.setup;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import pl.amon.tinymemory.TinyMemory;

@Mod.EventBusSubscriber(modid = TinyMemory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
  public static void init(final FMLCommonSetupEvent event) {
  }
}
