package pl.amon.tinymemory.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.screen.ProjectTableScreen;

@Mod.EventBusSubscriber(modid = TinyMemory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
  public static void init(final FMLClientSetupEvent event) {
    MenuScreens.register(Registration.PROJECT_TABLE_MENU.get(), ProjectTableScreen::new);
  }

  @SuppressWarnings("unused")
  @SubscribeEvent
  public static void onTextureStitch(TextureStitchEvent.Pre event) {
    if (!event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
      return;
    }
  }
}
