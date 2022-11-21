package pl.amon.tinymemory.setup;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.block.MemoryAssembler;
import pl.amon.tinymemory.block.Plate;
import pl.amon.tinymemory.block.ProjectTable;
import pl.amon.tinymemory.block.entity.ROM;
import pl.amon.tinymemory.items.MemoryBlueprint;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyMemory.MODID);
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TinyMemory.MODID);
  private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TinyMemory.MODID);

  //Items
  public static final RegistryObject<Item> MEMORY_BLUEPRINT_ITEM  = ITEMS.register("memory_blueprint", MemoryBlueprint::new);

  //Blocks
  public static final RegistryObject<Block> ROM_BLOCK = BLOCKS.register("rom", () -> new Plate<ROM>(ROM::new, ROM::getTicker));
  public static final RegistryObject<Block> MEMORY_ASSEMBLER_BLOCK = BLOCKS.register("memory_assembler", MemoryAssembler::new);
  public static final RegistryObject<Block> PROJECT_TABLE_BLOCK = BLOCKS.register("project_table", ProjectTable::new);
  
  //Block Entities
  public static final RegistryObject<BlockEntityType<ROM>> ROM_TILE = TILES.register("rom", () -> BlockEntityType.Builder.of(ROM::new, ROM_BLOCK.get()).build(null));

  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
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
