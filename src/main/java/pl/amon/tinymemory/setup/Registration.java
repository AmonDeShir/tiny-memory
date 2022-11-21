package pl.amon.tinymemory.setup;

import java.util.function.Supplier;

import com.dannyandson.tinyredstone.setup.ModSetup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.block.ROM;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyMemory.MODID);
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TinyMemory.MODID);

  public static final RegistryObject<Block> ROM = BLOCKS.register("rom", ROM::new);
  
  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  public static void registerPanelCells(){
    //TinyRedstone.registerPanelCell(ROM.class, TINY_ROM.get());
  }

  //Register an item for all blocks
  @SubscribeEvent
  public static void onRegisterItems(final RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
      BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
        Block block = blockRegistryObject.get();
        Item.Properties properties = new Item.Properties().tab(ModSetup.ITEM_GROUP);
        Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
        event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
      });
    }
  }
}
