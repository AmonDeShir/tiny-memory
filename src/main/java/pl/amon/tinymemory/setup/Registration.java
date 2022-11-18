package pl.amon.tinymemory.setup;

import com.dannyandson.tinygates.items.PanelCellGateItem;
import com.dannyandson.tinyredstone.TinyRedstone;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.elements.*;

public class Registration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyMemory.MODID);

  public static final RegistryObject<Item> TINY_ROM = ITEMS.register("tiny_rom", PanelCellGateItem::new);
  
  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  public static void registerPanelCells(){
    TinyRedstone.registerPanelCell(ROM.class, TINY_ROM.get());
  }
}
