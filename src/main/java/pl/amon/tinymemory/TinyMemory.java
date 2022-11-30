package pl.amon.tinymemory;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import pl.amon.tinymemory.network.ModNetworkHandler;
import pl.amon.tinymemory.setup.ClientSetup;
import pl.amon.tinymemory.setup.ModSetup;
import pl.amon.tinymemory.setup.Registration;
import org.slf4j.Logger;

@Mod(TinyMemory.MODID)
public class TinyMemory {
  public static final String MODID = "tinymemory";
  public static final Logger LOGGER = LogUtils.getLogger();


    public TinyMemory() {
      Registration.register();
      FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);

      if(FMLEnvironment.dist.isClient()) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
      }

      // Register the setup method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

      // Register ourselves for server and other game events we are interested in
      MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    // register everything
    Registration.registerPanelCells();
    ModNetworkHandler.registerMessages();
  }
}
